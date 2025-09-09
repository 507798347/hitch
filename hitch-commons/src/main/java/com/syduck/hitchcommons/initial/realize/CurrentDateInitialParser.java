package com.syduck.hitchcommons.initial.realize;

import com.syduck.hitchcommons.initial.annotation.InitialResolver;
import com.syduck.hitchcommons.initial.InitialParser;

import java.util.Date;

public class CurrentDateInitialParser implements InitialParser {

    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(Date.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return new Date();
    }
}
