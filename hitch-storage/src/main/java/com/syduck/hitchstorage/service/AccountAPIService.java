package com.syduck.hitchstorage.service;

import com.syduck.hitchcommons.initial.factory.InitialParserFactory;
import com.syduck.hitchmodules.po.AccountPO;
import com.syduck.hitchmodules.po.AuthenticationPO;
import com.syduck.hitchmodules.po.VehiclePO;
import com.syduck.hitchstorage.mapper.AccountMapper;
import com.syduck.hitchstorage.mapper.AuthenticationMapper;
import com.syduck.hitchstorage.mapper.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountAPIService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AuthenticationMapper authenticationMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    /**
     * 账户注册
     */
    @RequestMapping("/register")
    @Transactional
    public AccountPO register(@RequestBody AccountPO accountPO) {
        accountMapper.insert(accountPO);
        addAuthentication(accountPO);
        addVehicleAuth(accountPO);
        return accountPO;
    }

    /**
     * 账户修改
     */

    @RequestMapping("/update")
    public void update(@RequestBody AccountPO record) {
        accountMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 根据ID获取用户信息
     */
    @RequestMapping("/getAccountByID/{id}")
    public AccountPO getAccountByID(@PathVariable("id") String id) {
        return accountMapper.selectByPrimaryKey(id);
    }


    @RequestMapping("/checkLogin")
    public AccountPO checkLogin(@RequestBody AccountPO accountPO) {
        return accountMapper.checkLogin(accountPO);
    }

    /**
     * 添加用户认证信息
     */
    private void addAuthentication(AccountPO accountPO) {
        AuthenticationPO authenticationPO = (AuthenticationPO) InitialParserFactory.initialDefValueForPO(new AuthenticationPO());
        authenticationPO.setPhone(accountPO.getPhone());
        authenticationPO.setUseralias(accountPO.getUseralias());
        authenticationPO.setStatus("0");
        AuthenticationPO po = authenticationMapper.selectByPhone(accountPO.getPhone());
        if (null == po) {
            authenticationMapper.insert(authenticationPO);
        }
    }

    /**
     * 添加车辆认证信息
     */
    private void addVehicleAuth(AccountPO accountPO) {
        VehiclePO vehiclePO = (VehiclePO) InitialParserFactory.initialDefValueForPO(new VehiclePO());
        vehiclePO.setPhone(accountPO.getPhone());
        vehiclePO.setStatus(0);
        VehiclePO po = vehicleMapper.selectByPhone(accountPO.getPhone());
        if (null == po) {
            vehicleMapper.insert(vehiclePO);
        }
    }
}
