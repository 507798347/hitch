package com.syduck.hitchaccount.handler;

import com.syduck.hitchaccount.service.AccountAPIService;
import com.syduck.hitchcommons.domain.vo.response.ResponseVO;
import com.syduck.hitchcommons.enums.BusinessErrors;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import com.syduck.hitchcommons.utils.CommonsUtils;
import com.syduck.hitchcommons.utils.SnowflakeIdWorker;
import com.syduck.hitchmodules.po.AccountPO;
import com.syduck.hitchmodules.vo.AccountVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountAPIService accountAPIService;

    private final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

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
}
