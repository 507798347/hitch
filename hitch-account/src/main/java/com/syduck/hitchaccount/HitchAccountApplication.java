package com.syduck.hitchaccount;

import com.syduck.hitchcommons.initial.annotation.EnableRequestInitial;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.syduck.hitchaccount.service"})
@EnableRequestInitial // 自定义注解--开启AOP功能
public class HitchAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(HitchAccountApplication.class, args);
    }
}
