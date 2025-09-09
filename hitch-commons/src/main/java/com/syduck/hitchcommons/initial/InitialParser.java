package com.syduck.hitchcommons.initial;

import com.syduck.hitchcommons.initial.annotation.InitialResolver;

public interface InitialParser {
    boolean isMatch(Class clazz);
    Object getDefaultValue(Class clazz, InitialResolver initialResolver);
}
