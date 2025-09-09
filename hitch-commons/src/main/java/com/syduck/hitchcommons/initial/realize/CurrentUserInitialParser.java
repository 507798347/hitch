package com.syduck.hitchcommons.initial.realize;

import com.syduck.hitchcommons.constant.HitchConstants;
import com.syduck.hitchcommons.initial.InitialParser;
import com.syduck.hitchcommons.initial.annotation.InitialResolver;
import com.syduck.hitchcommons.utils.RequestUtils;

public class CurrentUserInitialParser implements InitialParser {

    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return RequestUtils.getRequestHeader(HitchConstants.HEADER_ACCOUNT_KEY);
    }
}
