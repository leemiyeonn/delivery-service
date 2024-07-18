package org.delivery.api.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ErrorCodeIfs {

    OK(HttpStatus.OK.value(), 200 , "Success"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "Bad Request"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "Internal Server Error"),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null Pointer Exception");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
