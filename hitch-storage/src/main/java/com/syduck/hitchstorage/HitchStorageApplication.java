package com.syduck.hitchstorage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.syduck.hitchstorage.mapper")
@EnableCaching
public class HitchStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitchStorageApplication.class, args);
    }

}
