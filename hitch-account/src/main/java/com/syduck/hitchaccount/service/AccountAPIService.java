package com.syduck.hitchaccount.service;

import com.syduck.hitchmodules.po.AccountPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="hitch-storage-server",path = "storage/account", contextId = "account")
public interface AccountAPIService {

    @RequestMapping("/register")
    AccountPO register(@RequestBody AccountPO accountPO);


    @RequestMapping("/checkLogin")
    AccountPO checkLogin(@RequestBody AccountPO accountPO);
}
