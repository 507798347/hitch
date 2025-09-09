package com.syduck.hitchorder;

import com.syduck.hitchcommons.initial.annotation.EnableRequestInitial;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.syduck.hitchorder.service"})
@EnableRequestInitial
public class HitchOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitchOrderApplication.class, args);
    }

}
