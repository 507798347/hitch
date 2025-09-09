package com.syduck.hitchcommons.initial.annotation;

import com.syduck.hitchcommons.enums.InitialResolverType;
import com.syduck.hitchcommons.groups.Group;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
@Documented
public @interface InitialResolver {
    InitialResolverType resolver();

    Class<?>[] groups() default Group.All.class;

    String def() default "";
}
