package com.syduck.hitchcommons.utils;

import com.syduck.hitchcommons.constant.HitchConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtils {
    /**
     * 获取Request对象
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    /**
     * 获取Head中的数据
     */
    public static String getRequestHeader(String headerName) {
        HttpServletRequest request = getRequest();
        return request.getHeader(headerName);
    }

    /**
     * 获取当前用户信息
     */
    public static String getCurrentUserId() {
        return getRequestHeader(HitchConstants.HEADER_ACCOUNT_KEY);
    }
}
