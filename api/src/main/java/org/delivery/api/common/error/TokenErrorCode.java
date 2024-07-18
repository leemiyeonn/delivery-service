package org.delivery.api.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCodeIfs {

    INVALID_TOKEN(400 , 2000 , "Invalid token"),
    EXPIRED_TOKEN(400, 2001, "Expired token"),
    TOKEN_EXCEPTION(400, 2002, "Unknown token error"),
    AUTHORIZATION_TOKEN_NOT_FOUND(400, 2003, "No token in authorization header");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
