package com.syduck.hitchstroke;

import com.syduck.hitchcommons.initial.annotation.EnableRequestInitial;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.syduck.hitchstroke.service"})
@EnableRequestInitial
@EnableDiscoveryClient
public class HitchStrokeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitchStrokeApplication.class, args);
    }

}
