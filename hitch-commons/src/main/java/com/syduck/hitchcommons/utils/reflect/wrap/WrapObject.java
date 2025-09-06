package com.syduck.hitchcommons.utils.reflect.wrap;

import java.util.Collection;

/*
* 持有一个原始对象（originalData）和它的类信息（wrapClass）。
*提供 getValue 方法，通过属性名获取值。
*提供 setValue 方法，通过属性名设置值。
*可以获取某个属性（getProperty）或所有属性（getPropertyList）
* */
public class WrapObject {
    private final WrapClass wrapClass;
    private final Object originalData;


    public WrapObject(Object originalData, WrapClass wrapClass) {
        if (originalData == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.originalData = originalData;
        this.wrapClass = wrapClass;

    }


    public Object getValue(String propName) {
        WrapProperty meateProp = wrapClass.getProperty(propName);
        if (null == meateProp) {
            return null;
        }
        return meateProp.getValue(originalData);
    }

    public void setValue(String propName, Object... args) {
        WrapProperty meateProp = wrapClass.getProperty(propName);
        if (null == meateProp) {
            return;
        }
        meateProp.setValue(originalData, args);
    }

    public WrapProperty getProperty(String key) {
        return wrapClass.getPropertyMap().get(key);
    }

    public Collection<WrapProperty> getPropertyList() {
        return wrapClass.getPropertyMap().values();
    }

}
