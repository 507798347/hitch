package com.syduck.hitchcommons.initial.realize;

import com.syduck.hitchcommons.initial.InitialParser;
import com.syduck.hitchcommons.initial.annotation.InitialResolver;
import com.syduck.hitchcommons.utils.SnowflakeIdWorker;

public class SnowflakeIDInitialParser implements InitialParser {

    SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(10, 11);

    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return String.valueOf(snowflakeIdWorker.nextId());
    }
}
