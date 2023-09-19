package com.highright.highcare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)

public class ResException extends RuntimeException {
    public ResException(String msg){
        super(msg);
    }
}
