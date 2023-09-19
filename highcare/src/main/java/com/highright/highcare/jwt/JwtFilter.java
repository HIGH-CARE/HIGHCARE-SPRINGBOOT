package com.highright.highcare.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
//@Component
//@Order(2)
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = ((HttpServletRequest)request).getRequestURI();
        log.info("[JwtFilter] doFilterInternal requestURI ==== {}", requestURI);
        // 1. 토큰 풀기
        String jwt = tokenProvider.resolveToken(request);

        log.info("[JwtFilter] doFilterInternal jwt ==== {}", jwt);

//        if(requestURI.contains("/bulletin/notice")){
//
//        }

        // 2. 추출한 토큰의 유효성 검사
        // 2-1. 만료된 토큰이면 refreshtoken을 추출하여 token을 재발급한다.
        // 2-2. 인증을 위해 Authentication 객체를 SecurityContextHolder에 담는다.

        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) ){

            log.info("[JwtFilter] validateToken 통과 ==== {통과}");
            // 인증
            Authentication authentication = tokenProvider.getAuthentication(jwt);

            // 권한부여
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[JwtFilter] resolveToken 완료 ==== {통과}");
        }

        filterChain.doFilter(request, response);

    }
}
