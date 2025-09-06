package com.syduck.hitchaccount.configuration;

import com.netflix.client.ClientException;
import com.syduck.hitchcommons.domain.vo.response.ResponseVO;
import com.syduck.hitchcommons.exception.BusinessRuntimeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

public class GlobalExceptionHandler {
    @ExceptionHandler(value = BusinessRuntimeException.class)
    @ResponseBody
    public ResponseVO<?> businessRuntimeException(BusinessRuntimeException e){return ResponseVO.error(e);}

    @ExceptionHandler(value = ClientException.class)
    @ResponseBody
    public ResponseVO<?> clientException(ClientException e){return ResponseVO.error(e.getErrorMessage());}

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseVO.error(String.join(",",errors));
    }
}
