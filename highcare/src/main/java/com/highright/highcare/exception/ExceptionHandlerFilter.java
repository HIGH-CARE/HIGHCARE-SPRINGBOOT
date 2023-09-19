package com.highright.highcare.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highright.highcare.exception.dto.ApiExceptionDTO;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
//@Order(1)
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("doFilter Internal doFilter Internal");
        try{
            filterChain.doFilter(request,response);
        } catch (Throwable ex) {
            handleException(request, response, ex);

//            filterChain.doFilter(request,response);

        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException {
        response.setContentType("application/json");
        int statusCode;

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        System.out.println(" 에러 핸들러========= " );
        if (ex instanceof TokenException) {
            // 토큰 관련 예외 처리
            statusCode = 1000;
            ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(statusCode, ex.getMessage());
            response.setStatus(statusCode);
            response.getWriter().write(new ObjectMapper().writeValueAsString(apiExceptionDTO));
        }
            else if (ex instanceof NotAllowedRequestException) {
                // 권한 없음 예외 처리
                statusCode = 1001;
                ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(statusCode, ex.getMessage());

                response.setStatus(statusCode);
//            response.sendError(statusCode);
                response.getWriter().write(new ObjectMapper().writeValueAsString(apiExceptionDTO));
        }
//            else if (ex instanceof LoginFailedException) {
//            statusCode = 403;
//            ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(statusCode,  ex.getMessage());
//            response.setStatus(statusCode);
//            response.getWriter().write(new ObjectMapper().writeValueAsString(apiExceptionDTO));
//        }
//            else if (ex instanceof IncorrectPasswordExceedException) {
//            statusCode = 1002;
//            ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(statusCode,  ex.getMessage());
//            response.setStatus(statusCode);
//            response.getWriter().write(new ObjectMapper().writeValueAsString(apiExceptionDTO));
//        }
//            else if (ex instanceof MethodArgumentNotValidException) {
//            statusCode = 1003;
//            ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(statusCode,  ex.getMessage());
//            response.setStatus(statusCode);
//            response.getWriter().write(new ObjectMapper().writeValueAsString(apiExceptionDTO));
//        }
//            else {
//            // 기타 예외 처리
//            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
//            ApiExceptionDTO apiExceptionDTO = new ApiExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
//            response.setStatus(statusCode);
//            response.getWriter().write(new ObjectMapper().writeValueAsString(apiExceptionDTO));
//        }
    }

}
