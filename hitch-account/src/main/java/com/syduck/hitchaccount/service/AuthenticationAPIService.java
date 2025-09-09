package com.syduck.hitchaccount.service;

import com.syduck.hitchmodules.po.AuthenticationPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@FeignClient(name = "hitch-stoge-server", path = "/storage/authentication", contextId = "authentication")
public interface AuthenticationAPIService {
    /**
     * 新增订单
     */
    @RequestMapping("/add")
    AuthenticationPO add(@RequestBody AuthenticationPO record);

    @RequestMapping("/update")
    void update(@RequestBody AuthenticationPO record);


    /**
     * 查询订单列表
     */
    @RequestMapping("/selectList")
    List<AuthenticationPO> selectlist(@RequestBody AuthenticationPO record);


    /**
     * 根据ID查看订单
     */
    @RequestMapping("/selectByID/{id}")
    AuthenticationPO selectByID(@PathVariable("id") String id);

    /**
     * 根据手机号号码查询认证信息
     */
    @RequestMapping("/selectByPhone/{phone}")
    AuthenticationPO selectByPhone(@PathVariable("phone") String phone);
}
