package com.syduck.hitchcommons.initial.realize;

import com.syduck.hitchcommons.initial.InitialParser;
import com.syduck.hitchcommons.initial.annotation.InitialResolver;
import com.syduck.hitchcommons.utils.reflect.ReflectUtils;

public class DefaultValueInitialParser implements InitialParser {
    @Override
    public boolean isMatch(Class clazz) {
        return ReflectUtils.isBasicTypes(clazz);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        Object defValue = null;
        try {
            defValue = ReflectUtils.getDefValue(clazz, initialResolver.def());
        } catch (Exception ignored) {}
        return defValue;
    }
}
