package com.example.ggsb_back.global.error;

import com.example.ggsb_back.global.error.exception.InterServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(InterServerException.class)
    public ResponseEntity<ErrorResponseDto> handleInterServerException(InterServerException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        log.info("{}", "hi");
        return new ResponseEntity<>(responseDto, errorCode.getStatus());
    }

}
