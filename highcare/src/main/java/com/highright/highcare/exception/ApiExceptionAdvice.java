package com.highright.highcare.exception;


import com.highright.highcare.admin.entity.AccessManager;
import com.highright.highcare.admin.repository.AccessManagerRepository;
import com.highright.highcare.exception.dto.ApiExceptionDTO;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// 예외 처리용 어드바이스
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionAdvice {

    private final AccessManagerRepository accessManagerRepository;

    // 로그인 관련 오류 2001
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ApiExceptionDTO> exceptionHandler(LoginFailedException e){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiExceptionDTO(2001, e.getMessage()));
    }

    @ExceptionHandler(LoginFailedPasswordException.class)
    public ResponseEntity<ApiExceptionDTO> exceptionHandler(LoginFailedPasswordException e){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiExceptionDTO(2001, e.getMessage()));
    }

    // 패스워드 5회 이상 오류
    @ExceptionHandler(IncorrectPasswordExceedException.class)
    public ResponseEntity<ApiExceptionDTO> incorrectPasswordExceedExceptions(IncorrectPasswordExceedException e) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiExceptionDTO(2001, e.getMessage()));

    }



    // 토큰 오류 2002
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ApiExceptionDTO> exceptionHandler(TokenException e){


        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiExceptionDTO(2002, e.getMessage()));
    }


    // 권한 오류 2003
    @ExceptionHandler(NotAllowedRequestException.class)
    public ResponseEntity<ApiExceptionDTO> handleNotAllowedRequestExceptions(NotAllowedRequestException e){

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiExceptionDTO(2003, e.getMessage()));
    }



    // 유효성 검사 관련 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 유효성 검사에 실패한 경우 예외 처리
        StringBuilder errorMessage = new StringBuilder(" 2004 : ");
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorMessage.append(fieldError.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
            } else {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
        }
        return ResponseEntity.badRequest().body(new ApiExceptionDTO(2004, errorMessage.toString()));
    }





}
