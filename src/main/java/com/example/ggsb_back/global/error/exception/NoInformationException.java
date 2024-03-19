package com.example.ggsb_back.global.error.exception;

import com.example.ggsb_back.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class NoInformationException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoInformationException() {
        this.errorCode = ErrorCode.NO_INFORMATION_ERROR;
    }

}
