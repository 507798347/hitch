package com.syduck.hitchcommons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ResponseState {
    SUCCESS(200, "成功"),
    ERROR(-1, "失败"),
    UNKNOWN(-2, "未知错误");

    private int code;
    private String errorMsg;

    public void setCode(int code) {
        this.code = code;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
