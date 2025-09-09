package com.syduck.hitchcommons.initial.aspect;


import com.syduck.hitchcommons.initial.factory.InitialParserFactory;
import com.syduck.hitchcommons.initial.annotation.RequestInitial;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;


@Aspect
public class RequestInitialAspect {
    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.syduck.hitchcommons.initial.annotation.RequestInitial)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        RequestInitial requestInitial = getRequestInitial(pjp);
        Object[] parameterValues = pjp.getArgs();
        if (null != requestInitial) {
            //拦截后给属性赋值
            InitialParserFactory.initialDefValue(parameterValues, requestInitial);
        }
        return pjp.proceed(parameterValues);
    }

    private RequestInitial getRequestInitial(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        return method.getAnnotation(RequestInitial.class);
    }

}
