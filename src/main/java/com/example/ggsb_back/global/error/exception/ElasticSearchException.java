package com.example.ggsb_back.global.error.exception;

import com.example.ggsb_back.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class ElasticSearchException extends RuntimeException{

    private final ErrorCode errorCode;

    public ElasticSearchException() {
        this.errorCode = ErrorCode.ELASTIC_SEARCH_ERROR;
    }

}
