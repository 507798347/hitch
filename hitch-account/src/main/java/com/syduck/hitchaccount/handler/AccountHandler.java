package com.syduck.hitchaccount.handler;

import com.syduck.hitchaccount.service.VehicleAPIService;
import com.syduck.hitchaccount.service.AccountAPIService;
import com.syduck.hitchaccount.service.AuthenticationAPIService;
import com.syduck.hitchcommons.domain.vo.response.ResponseVO;
import com.syduck.hitchcommons.entity.SessionContext;
import com.syduck.hitchcommons.enums.BusinessErrors;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import com.syduck.hitchcommons.helper.RedisSessionHelper;
import com.syduck.hitchcommons.template.SessionTemplate;
import com.syduck.hitchcommons.utils.CommonsUtils;
import com.syduck.hitchcommons.utils.RequestUtils;
import com.syduck.hitchcommons.utils.SnowflakeIdWorker;
import com.syduck.hitchmodules.po.AccountPO;
import com.syduck.hitchmodules.po.AuthenticationPO;
import com.syduck.hitchmodules.po.VehiclePO;
import com.syduck.hitchmodules.vo.AccountVO;
import com.syduck.hitchmodules.vo.AuthenticationVO;
import com.syduck.hitchmodules.vo.VehicleVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountAPIService accountAPIService;
    private final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
    private final static Logger logger = LoggerFactory.getLogger(AccountHandler.class);

    private final RedisSessionHelper redisSessionHelper;

    private final SessionTemplate sessionTemplate;

    private final AuthenticationAPIService authenticationAPIService;

    private final VehicleAPIService vehicleAPIService;

    private final AiHelper aiHelper;
    //用户注册
    public ResponseVO<AccountVO> register(AccountVO accountVO){
        //用户名、手机号、密码不能为空
        if (StringUtils.isAnyEmpty(accountVO.getUsername(),accountVO.getPhone(),accountVO.getPassword())){
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY);
        }
        //检查是否已经存在
        AccountPO accountPO = accountAPIService.checkLogin(CommonsUtils.toPO(accountVO));
        if (ObjectUtils.isEmpty(accountPO)){
            throw new BusinessRuntimeException(BusinessErrors.DATA_DUPLICATION);
        }

        accountVO.setPassword(CommonsUtils.encodeMD5(accountVO.getPassword()));
        accountVO.setId(String.valueOf(idWorker.nextId()));
        accountVO.setRole(0); // 默认身份为乘客 车主待认证后为1
        accountVO.setStatus(0); // 待实名认证后改为1

        AccountPO result = accountAPIService.register(CommonsUtils.toPO(accountVO));
        return ResponseVO.success(result);
    }
    //用户登录
    public ResponseVO accountLogin(AccountVO accountVO) {
        AccountVO vo = verifyAccountLogin(accountVO);
        SessionContext sessionContext = redisSessionHelper.createSession(vo, vo.getId(), vo.getUsername(), vo.getUseralias(), null);
        vo.setToken(sessionContext.getSessionID());
        return ResponseVO.success(vo);
    }
    //验证前端传来的登录信息
    private AccountVO verifyAccountLogin(AccountVO accountVO) {
        //用户或者手机号不能为空
        if (StringUtils.isAllEmpty(accountVO.getUsername(), accountVO.getPhone())) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY);
        }
        //密码不能为空
        if (StringUtils.isEmpty(accountVO.getPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY, "密码不能为空");
        }
        //查询用户登录
        AccountPO accountPO = accountAPIService.checkLogin(CommonsUtils.toPO(accountVO));
        //非空校验
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        //验证密码
        if (!CommonsUtils.encodeMD5(accountVO.getPassword()).equals(accountPO.getPassword())) {
            logger.warn("password error! account=" + accountPO.getUseralias());
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY, "用户名或者密码错误");
        }
        //验证通过 返回VO
        return (AccountVO) CommonsUtils.toVO(accountPO);
    }
    //用户修改密码
    public ResponseVO<AccountVO> modifyPassword(AccountVO accountVO) {
        //获取当前登录用户的id,从后台获取session里的用户
        SessionContext sessionByAccount = sessionTemplate.getSessionByAccount(accountVO.getCurrentUserId());
        String accountID = sessionByAccount.getAccountID();
        logger.info("用户修改密码，当前用户id为：{}", accountID);
        //String userid = accountVO.getCurrentUserId();
        if (StringUtils.isAnyEmpty(accountVO.getPassword(), accountVO.getNewPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "新老密码不允许为空");
        }
        //获取当前用户在数据库里的信息
        AccountPO account = accountAPIService.getAccountByID(accountID);
        if (account == null) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        //旧新密码加密，对比数据库，防止输入错误
        String oldPassword = DigestUtils.md5DigestAsHex(accountVO.getPassword().getBytes());
        String newPassword = DigestUtils.md5DigestAsHex(accountVO.getNewPassword().getBytes());

        if (!oldPassword.equals(account.getPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR, "旧密码输入错误");
        }

        //校验通过，将新密码写入数据库，修改成功
        account.setPassword(newPassword);
        accountAPIService.update(account);

        return ResponseVO.success(null, "修改密码成功");
    }
    //修改用户信息
    public ResponseVO<AccountVO> modify(AccountVO accountVO) {
        AccountPO accountPO = accountAPIService.getAccountByID(accountVO.getCurrentUserId());
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        AccountPO po = CommonsUtils.toPO(accountVO);
        po.setId(accountPO.getId());
        accountAPIService.update(po);
        return ResponseVO.success(null, "修改用户信息成功");
    }
    //获取用户基本信息
    public ResponseVO<AccountVO> userinfo() {
        AccountPO accountPO = getCurrentAccountPO();
        return ResponseVO.success(accountPO);
    }
    //获取当前登录用户
    private AccountPO getCurrentAccountPO() {
        String userId = RequestUtils.getCurrentUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR);
        }
        AccountPO accountPO = accountAPIService.getAccountByID(userId);
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        return accountPO;
    }
    //获取用户认证信息
    public ResponseVO<AuthenticationVO> getAuthenticationInfo() {
        AccountPO accountPO = getCurrentAccountPO();
        AuthenticationPO authenticationPO = getAuthenticationPO(accountPO);
        if (null == authenticationPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        return ResponseVO.success(authenticationPO);
    }
    //获取用户认证对象
    private AuthenticationPO getAuthenticationPO(AccountPO accountPO) {
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        return authenticationAPIService.selectByPhone(accountPO.getPhone());
    }
    //修改用户认证信息
    public ResponseVO<AuthenticationVO> modifyAuthentication(AuthenticationVO authenticationVO) {
        //根据token获取当前登录用户的信息对象
        AccountPO accountPO = getCurrentAccountPO();
        //根据accountPO手机号查询出认证信息
        AuthenticationPO authenticationPO = getAuthenticationPO(accountPO);
        if (null == authenticationPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        AuthenticationPO updatePo = CommonsUtils.toPO(authenticationVO);
        updatePo.setId(authenticationPO.getId());
        authenticationAPIService.update(updatePo);
        return ResponseVO.success(updatePo);
    }
    //获取车辆认证信息
    public ResponseVO<VehicleVO> getVehicleInfo() {
        AccountPO accountPO = getCurrentAccountPO();
        VehiclePO vehiclePO = getVehiclePO(accountPO);
        if (null == vehiclePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        return ResponseVO.success(vehiclePO);
    }
    //获取车辆基本信息
    private VehiclePO getVehiclePO(AccountPO accountPO) {
        return vehicleAPIService.selectByPhone(accountPO.getPhone());
    }
    //修改车辆信息
    public ResponseVO<VehicleVO> modifyVehicle(VehicleVO vehicleVO) {
        AccountPO accountPO = getCurrentAccountPO();
        VehiclePO vehiclePO = getVehiclePO(accountPO);
        if (null == vehiclePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "车辆认证信息不存在");
        }
        VehiclePO updatePo = CommonsUtils.toPO(vehicleVO);
        updatePo.setId(vehiclePO.getId());
        vehicleAPIService.update(updatePo);
        return ResponseVO.success(updatePo);
    }
    //验证Token
    public ResponseVO verifyToken(String sessionID) {
        SessionContext sessionContext = redisSessionHelper.getSession(sessionID);
        if (null == sessionContext) {
            throw new BusinessRuntimeException(BusinessErrors.TOKEN_IS_INVALID);
        }
        if (StringUtils.isEmpty(sessionContext.getAccountID())) {
            throw new BusinessRuntimeException(BusinessErrors.TOKEN_IS_INVALID);
        }
        AccountPO accountPO = accountAPIService.getAccountByID(sessionContext.getAccountID());
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }

        return ResponseVO.success(accountPO);
    }
    //个人身份认证
    public ResponseVO<AuthenticationVO> identityAuth() {
        //获取 现在登陆的（解析token 拿到用户ID 根据ID获取对象） 用户对象
        AccountPO accountPO = getCurrentAccountPO();
        //通过accountPO里的手机号 查询出 认证对象
        AuthenticationPO authenticationPO = getAuthenticationPO(accountPO);
        if (null == authenticationPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        //拿出 认证对象中的 身份证正反面
        String cardIdFrontPhotoAddr = authenticationPO.getCardIdFrontPhoto();
        if (StringUtils.isEmpty(cardIdFrontPhotoAddr)) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "身份证正面照片不存在");
        }

        //TODO:任务2.2-个人实名认证（选做）
        //【可选作业】：调百度完成身份证识别，将识别信息更新到数据库对应字段
        //文档（身份证识别）：https://cloud.baidu.com/doc/OCR/s/rk3h7xzck
        //文档（h5人脸实名认证接口）：https://ai.baidu.com/ai-doc/FACE/skxie72kp
        //备注：真实的实名认证需要企业身份，个人无法使用。


        //真实业务需要设置Account用户真实姓名，这里直接用用户名
        accountPO.setUseralias(accountPO.getUsername());
        accountPO.setStatus(1); //状态改成已认证
        //更新Redis缓存
        sessionTemplate.updateSessionUseralias(accountPO.getId(), accountPO.getUseralias());
        accountAPIService.update(accountPO);
        authenticationAPIService.update(authenticationPO);
        return ResponseVO.success(authenticationPO);
    }
    //车牌识别
    public ResponseVO<VehicleVO> vehicleAuth() {
        AccountPO accountPO = getCurrentAccountPO();
        VehiclePO vehiclePO = getVehiclePO(accountPO);
        try {
            // TODO:任务2.1-车辆信息验证入口-2day
            String license = aiHelper.getLicense(vehiclePO);
            vehiclePO.setCarNumber(license);
            accountPO.setRole(1);
            accountAPIService.update(accountPO);
            vehicleAPIService.update(vehiclePO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseVO.error(e.getMessage());
        }
        return ResponseVO.success(vehiclePO);
    }
}
