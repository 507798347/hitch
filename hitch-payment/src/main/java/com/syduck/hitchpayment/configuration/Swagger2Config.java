package com.syduck.hitchpayment.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

//访问地址：http://localhost:8888/doc.html
@Configuration
@EnableSwagger2WebMvc
public class Swagger2Config {

    @Value("${spring.application.name}")
    private String appName;



    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//                   当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.syduck.payment.web"))
                .paths(PathSelectors.any()).build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("AA顺风车 - " + appName)
                //创建人
                .contact(new Contact("Wang", null, "wang@example.cn"))
                //版本号
                .version("1.0")
                //描述
                .description("")
                .build();
    }
}