package com.syduck.hitchcommons.initial.annotation;

import com.syduck.hitchcommons.groups.Group;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface RequestInitial {
    Class<?>[] groups() default Group.All.class;
}
