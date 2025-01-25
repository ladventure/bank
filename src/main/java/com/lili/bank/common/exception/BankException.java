package com.lili.bank.common.exception;

import lombok.Data;
import lombok.Getter;

/**
 * custom exception
 */
@Data
public class BankException extends RuntimeException{

    private BankExceptionEnum error;

    public BankException(BankExceptionEnum error) {
        this.error = error;
    }
}
