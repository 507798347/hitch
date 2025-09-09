package com.syduck.hitchcommons.utils.reflect.wrap;

import lombok.Getter;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
public class WrapProperty {


    private String propName;
    private Class dataType;
    private Method readMethod;
    private Method writeMethod;
    private Annotation[] annotations;


    public WrapProperty(PropertyDescriptor propertyDescriptor) {
        this.propName = propertyDescriptor.getName();
        this.dataType = propertyDescriptor.getPropertyType();
        this.readMethod = propertyDescriptor.getReadMethod();
        this.writeMethod = propertyDescriptor.getWriteMethod();
    }

    public Object getValue(Object originalData) {
        if (null == readMethod) {
            return null;
        }
        try {
            return readMethod.invoke(originalData);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(Object originalData, Object... args) {

        boolean isSuccess = false;
        if (null == writeMethod) {
            return;
        }
        try {
            writeMethod.invoke(originalData, args);
            isSuccess = true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

}
