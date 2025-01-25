package com.lili.bank.common.exception;

import lombok.Getter;

/**
 * define all exception，according to compile different exception enum to change error message
 * just for test, I define a few ,use common exception replace。
 */
public enum BankExceptionEnum {
    COMMON_ERROR(10000, "COMMON ERROR"),
    NOT_FOUND_ERROR(10001, "NOT FOUND ERROR"),
    CONCURRENT_ERROR(10002, "CONCURRENT ERROR"),
    DUPLICATE_ERROR(10003, "DUPLICATE ERROR"),
    RECORD_OVER_LIMIT_ERROR(10004, "RECORD OVER LIMIT ERROR"),
        ;
    @Getter
    private int code;
    @Getter
    private String message;
    private BankExceptionEnum(int code,String message) {
        this.message = message;
        this.code = code;
    }
}
