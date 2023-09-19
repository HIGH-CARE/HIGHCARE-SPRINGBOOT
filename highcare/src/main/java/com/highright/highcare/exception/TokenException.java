package com.highright.highcare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TokenException extends RuntimeException {

    public TokenException(String msg) {
        super(msg);
    }
}
