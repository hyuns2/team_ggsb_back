package com.example.ggsb_back.global.error.exception;

import com.example.ggsb_back.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadPurificationException extends RuntimeException{
    private final ErrorCode errorCode;

    public BadPurificationException() {
        this.errorCode = ErrorCode.BAD_PURIFICATION_ERROR;
    }

}
