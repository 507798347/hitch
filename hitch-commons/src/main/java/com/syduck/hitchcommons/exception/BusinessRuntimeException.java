package com.syduck.hitchcommons.exception;

import com.syduck.hitchcommons.enums.BusinessErrors;
import lombok.Getter;

@Getter
public class BusinessRuntimeException extends RuntimeException{

    private final BusinessErrors businessErrors;

    public BusinessRuntimeException(BusinessErrors businessErrors){
        super(businessErrors.getMsg());
        this.businessErrors = businessErrors;
    }

    public BusinessRuntimeException(BusinessErrors businessError, String message) {
        super(message);
        this.businessErrors = businessError;
    }

}
