package com.example.ggsb_back.global.error.exception;

import com.example.ggsb_back.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class InterServerException extends RuntimeException{

    private final ErrorCode errorCode;

    public InterServerException() {
        this.errorCode = ErrorCode.INTER_SERVER_ERROR;
    }

}
