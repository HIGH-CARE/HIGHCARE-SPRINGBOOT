package com.highright.highcare.config;

import com.highright.highcare.jwt.JwtAccessDeniedHandler;
import com.highright.highcare.jwt.JwtAuthenticationEntryPoint;
import com.highright.highcare.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity  // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 403 code
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;            // 401 code

    @Bean // 비밀번호 암호화 처리 빈 등록
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정무시 정적 리소스 빈 등록
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().antMatchers("/css/**", "/js/**", "/images/**",
                "/lib/**");
    }

    // http 요청 권한설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            .and()
                .authorizeRequests()
                .antMatchers("/").authenticated()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/api/**").authenticated()
//                .antMatchers("/api/v0/**").permitAll() // v0 모든 접근 허용 (로그인창)
//                .antMatchers("/api/v1/**").hasAnyRole("USER", "MANAGER", "ADMIN") // v1: 일반 회원 이상 접근 가능
//                .antMatchers("/api/v2/**").hasRole("MANAGER")   // v2: 매니저- 각 부서 부장급 접근 가능
//                .antMatchers("/api/v3/**").hasRole("ADMIN")    // v3: 관리자 - 시스템운영담당자만 접근 가능
                .anyRequest().permitAll()   // 테스트 후 삭제
                .and()
                .apply(new JwtSecurityConfig(tokenProvider)) // jwt시큐리티설정파일 적용하기
                ;
                // oauth2 추가하기

        return http.build();

    }

    // cors config
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Content-type"
                , "Access-Control-Allow-Headers", "Authorization"
                , "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }



}