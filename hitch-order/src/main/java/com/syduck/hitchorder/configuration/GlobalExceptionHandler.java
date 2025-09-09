package com.syduck.hitchorder.configuration;

import com.netflix.client.ClientException;
import com.syduck.hitchcommons.domain.vo.response.ResponseVO;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessRuntimeException.class)
    @ResponseBody
    public ResponseVO businessRuntimeException(HttpServletRequest req, BusinessRuntimeException e) {
        return ResponseVO.error(e);
    }

    @ExceptionHandler(value = ClientException.class)
    @ResponseBody
    public ResponseVO clientException(HttpServletRequest req, ClientException e) {
        return ResponseVO.error(e.getErrorMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO methodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {
        // 获取所有异常
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseVO.error(String.join(",", errors));
    }


}