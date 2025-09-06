package com.syduck.hitchcommons.utils.reflect.wrap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/*维护一个缓存（wrapClassMap），存储类的信息（WrapClass），避免重复创建。
通过 getWarpObject 方法，根据输入的对象创建对应的 WrapObject*/
public class WrapObjectFactory {
    private static final Map<Class, WrapClass> wrapClassMap = new ConcurrentHashMap<>();

    public static WrapObject getWarpObject(Object orginObject) {
        WrapClass wrapClass = getClass(orginObject.getClass());
        return new WrapObject(orginObject, wrapClass);
    }

    private static WrapClass getClass(Class clazz) {
        return wrapClassMap.computeIfAbsent(clazz, WrapClass::new);
    }
}
