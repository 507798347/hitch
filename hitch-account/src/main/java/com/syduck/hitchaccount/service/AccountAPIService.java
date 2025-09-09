package com.syduck.hitchaccount.service;

import com.syduck.hitchmodules.po.AccountPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="hitch-storage-server",path = "storage/account", contextId = "account")
public interface AccountAPIService {

    @RequestMapping("/register")
    AccountPO register(@RequestBody AccountPO accountPO);
    /**
     * 账户修改
     */
    @RequestMapping("/update")
    public void update(@RequestBody AccountPO record);
    /**
     * 获取账户信息
     */
    @RequestMapping("/getAccountByID/{id}")
    public AccountPO getAccountByID(@PathVariable("id") String id);

    @RequestMapping("/checkLogin")
    AccountPO checkLogin(@RequestBody AccountPO accountPO);
}
