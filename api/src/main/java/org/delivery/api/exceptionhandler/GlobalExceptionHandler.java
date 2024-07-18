package org.delivery.api.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.delivery.api.common.api.Api;
import org.delivery.api.common.error.ErrorCode;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(value = Integer.MAX_VALUE) // 가장 마지막에 실행
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 모든 예외를 처리하는 핸들러 메서드
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception (Exception exception) {

        log.error("", exception);
        // 서버 에러 응답 생성 및 반환
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        Api.ERROR(ErrorCode.SERVER_ERROR)
                );
    }
}
