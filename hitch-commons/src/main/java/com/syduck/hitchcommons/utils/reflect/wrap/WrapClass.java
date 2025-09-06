package com.syduck.hitchcommons.utils.reflect.wrap;

import com.syduck.hitchcommons.utils.reflect.ReflectUtils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WrapClass {
    private final Class classType;
    private final Map<String, WrapProperty> propertyMap = new ConcurrentHashMap<>();
    private final Map<String, Field> fieldMap = new ConcurrentHashMap<>();

    public WrapClass(Class clazz) {
        this.classType = clazz;
        List<Field> fieldList = ReflectUtils.getFieldList(clazz);
        fieldList.forEach(this::addField);
        List<WrapProperty> propertyList = ReflectUtils.getWrapPropertyList(clazz);
        assert propertyList != null;
        propertyList.forEach(this::addProperty);

    }

    private void addProperty(WrapProperty prop) {
        if (!propertyMap.containsKey(prop.getPropName())) {
            Annotation[] annotations = ReflectUtils.getAnnotations(fieldMap.get(prop.getPropName()));
            prop.setAnnotations(annotations);
            propertyMap.put(prop.getPropName(), prop);
        }
    }

    private void addField(Field field) {
        if (!fieldMap.containsKey(field.getName())) {
            fieldMap.put(field.getName(), field);
        }
    }

    public WrapProperty getProperty(String propName) {
        return propertyMap.get(propName);
    }


}
