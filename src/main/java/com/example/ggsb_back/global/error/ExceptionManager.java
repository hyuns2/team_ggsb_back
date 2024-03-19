package com.example.ggsb_back.global.error;

import com.example.ggsb_back.global.error.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(InterServerException.class)
    public ResponseEntity<ErrorResponseDto> handleInterServerException(InterServerException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        return new ResponseEntity<>(responseDto, errorCode.getStatus());
    }

    @ExceptionHandler(BadLocationException.class)
    public ResponseEntity<ErrorResponseDto> handleBadLocationException(BadLocationException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        return new ResponseEntity<>(responseDto, errorCode.getStatus());
    }

    @ExceptionHandler(BadPurificationException.class)
    public ResponseEntity<ErrorResponseDto> handleBadPurificationException(BadPurificationException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        return new ResponseEntity<>(responseDto, errorCode.getStatus());
    }

    @ExceptionHandler(BadRangeException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRangeException(BadRangeException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        return new ResponseEntity<>(responseDto, errorCode.getStatus());
    }

    @ExceptionHandler(ElasticSearchException.class)
    public ResponseEntity<ErrorResponseDto> handleElasticSearchException(ElasticSearchException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        return new ResponseEntity<>(responseDto, errorCode.getStatus());
    }

    @ExceptionHandler(NoInformationException.class)
    public ResponseEntity<ErrorResponseDto> handleNoInformationException(NoInformationException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode);
        return new ResponseEntity<>(responseDto, errorCode.getStatus());
    }

}
