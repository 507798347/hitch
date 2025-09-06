package com.syduck.hitchcommons.groups;

public class Group {
    /**
     * 创建时进行校验
     */
    public interface Create{}
    /**
     * 更新时进行校验
     */
    public interface Update{}
    /**
     * 删除时进行校验
     */
    public interface Delete{}
    /**
     * 查询时进行校验
     */
    public interface Select{}
    /**
     * 所有条件时进行校验
     */
    public interface All{}
}
