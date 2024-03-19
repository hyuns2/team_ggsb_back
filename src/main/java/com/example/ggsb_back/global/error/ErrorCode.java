package com.example.ggsb_back.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_LOCATION_ERROR(401, "지역 정보를 잘못 입력하셨습니다.", HttpStatus.BAD_REQUEST),
    BAD_PURIFICATION_ERROR(402, "해당하는 정수장이 없습니다.", HttpStatus.BAD_REQUEST),
    BAD_RANGE_ERROR(403, "범위 번호를 확인해주세요.", HttpStatus.BAD_REQUEST),
    INTER_SERVER_ERROR(500, "INTER SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    ELASTIC_SEARCH_ERROR(501, "ELK에 문제가 있습니다.", HttpStatus.BAD_REQUEST),
    NO_INFORMATION_ERROR(502, "해당하는 지역 정수장의 정보가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus status;

}
