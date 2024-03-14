package com.example.ggsb_back.global.error.exception;

import com.example.ggsb_back.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BadLocationException extends RuntimeException{

    private final ErrorCode errorCode;

    public BadLocationException() {
        this.errorCode = ErrorCode.BAD_LOCATION_ERROR;
    }

}
