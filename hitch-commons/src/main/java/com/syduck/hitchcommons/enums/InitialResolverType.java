package com.syduck.hitchcommons.enums;

import com.syduck.hitchcommons.initial.realize.CurrentDateInitialParser;
import com.syduck.hitchcommons.initial.realize.CurrentUserInitialParser;
import com.syduck.hitchcommons.initial.realize.DefaultValueInitialParser;
import com.syduck.hitchcommons.initial.realize.SnowflakeIDInitialParser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InitialResolverType {

    CURRENT_DATE("CURRENT_DATE", CurrentDateInitialParser.class),
    CURRENTA_ACCOUNT("CURRENTA_ACCOUNT", CurrentUserInitialParser.class),
    GEN_SNOWFLAKE_ID("GEN_SNOWFLAKE_ID", SnowflakeIDInitialParser.class),
    DEF_VALUE("DEF_VALUE", DefaultValueInitialParser.class);


    //错误码
    private String resolverName;
    //具体错误信息
    private Class resolverClass;


    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
    }

    public void setResolverClass(Class resolverClass) {
        this.resolverClass = resolverClass;
    }
}
