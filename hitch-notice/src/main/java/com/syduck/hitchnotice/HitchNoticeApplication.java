package com.syduck.hitchnotice;

import com.syduck.hitchcommons.initial.annotation.EnableRequestInitial;
import com.syduck.hitchcommons.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//开启fegin支持，clients是指哪个类开启fegin
@EnableFeignClients(basePackages = {"com.syduck.hitchnotice.service"})
@EnableRequestInitial
@Import(SpringUtil.class)
public class HitchNoticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitchNoticeApplication.class, args);
    }

}
