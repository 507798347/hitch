package com.syduck.hitchcommons.initial.annotation;

import com.syduck.hitchcommons.initial.aspect.RequestInitialAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(RequestInitialAspect.class)
@Documented
public @interface EnableRequestInitial {
    // 没有参数
}
