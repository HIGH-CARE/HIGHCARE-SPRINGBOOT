package com.highright.highcare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(code = HttpStatus.BAD_REQUEST) // 기존 500 200으로 체인지
public class NotAllowedRequestException extends RuntimeException {
    public NotAllowedRequestException(String message) {
        super(message);

    }

}
