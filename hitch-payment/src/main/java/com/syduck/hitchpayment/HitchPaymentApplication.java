package com.syduck.hitchpayment;

import com.syduck.hitchcommons.initial.annotation.EnableRequestInitial;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRequestInitial
@EnableFeignClients(basePackages = {"com.syduck.hitchpayment.service"})
public class HitchPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitchPaymentApplication.class, args);
    }

}
