package com.syduck.hitchcommons.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalCollectionUtils {

    //创建一个新的List
    public static <T> List<T> emptyList() {
        return new ArrayList<>();
    }

    //数组转List
    @SafeVarargs
    public static <T> List<T> toList(T... arrays) {
        List<T> tList = emptyList();
        tList.addAll(Arrays.asList(arrays)); //Arrays.asList() 把一个数组快速转换成一个 List（列表
        return tList;
    }

    public static <T> T getFirst(List<T> tList) {
        if (null != tList && !tList.isEmpty()) {
            return tList.stream().findFirst().get();
        }
        return null;
    }

    public static String toString(List<String> stringList) {
        if (null == stringList) {
            return null;
        }
        if (stringList.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder(); // StringBuilder线程安全
        for (String str : stringList) {
            sb.append(str).append(",");
        }
        return StringUtils.removeEnd(sb.toString(), ",");
    }

    public static <T> T getOne(List<T> tList) {
        if (null != tList && !tList.isEmpty()) {
            return tList.stream().findFirst().get();
        }
        return null;
    }
}
