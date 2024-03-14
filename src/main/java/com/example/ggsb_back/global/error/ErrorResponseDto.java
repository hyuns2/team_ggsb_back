package com.example.ggsb_back.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDto {

    private int code;
    private String message;
    private HttpStatus status;

    public ErrorResponseDto(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

}
