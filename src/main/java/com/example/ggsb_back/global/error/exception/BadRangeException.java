package com.example.ggsb_back.global.error.exception;

import com.example.ggsb_back.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadRangeException extends RuntimeException{

    private final ErrorCode errorCode;

    public BadRangeException() {
        this.errorCode = ErrorCode.BAD_RANGE_ERROR;
    }

}
