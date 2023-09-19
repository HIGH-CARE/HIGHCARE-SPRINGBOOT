package com.highright.highcare.config;

import com.highright.highcare.auth.service.CustomUserDetailsService;
//import com.highright.highcare.exception.ExceptionHandlerFilter;
import com.highright.highcare.exception.ExceptionHandlerFilter;
import com.highright.highcare.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity  // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 403 code
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;            // 401 code
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final SpecificUrlFilter specificUrlFilter;
//    private final JwtFilter jwtFilter;


    private final CustomUserDetailsService customUserDetailsService;

    public static String hostname = "highcare.coffit.today:3000";
//public static String hostname = "http://localhost:3000";

    // 시큐리티 설정무시 정적 리소스 빈 등록
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().antMatchers("/css/**", "/js/**", "/images/**",
                "/lib/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/oauth/**").permitAll()
                .antMatchers("/bulletin/notice").permitAll()
                .antMatchers("/bulletin/board?categoryCode=4").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")    // 관리자 - 시스템운영담당자만 접근 가능
                .antMatchers("/api/**").hasAnyRole("USER", "MANAGER", "ADMIN") //일반 회원 이상 접근 가능
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .cors()
            .and()
                .logout().logoutSuccessUrl("/")
                .and().apply(new JwtSecurityConfig(tokenProvider))
                ;

        return http.build();
    }

    // cors config
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 출처(origin) 목록
        config.setAllowedOrigins(Arrays.asList(
                "http://highcare.coffit.today:8080",
                "https://kauth.kakao.com",
                "https://kapi.kakao.com",
                "http://highcare.coffit.today:3000",
                "https://highcare.coffit.today:3000"
        ));

        // 허용할 HTTP 메서드 목록
        config.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "OPTIONS", "DELETE"));

        // 허용할 HTTP 헤더 목록
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "X-Requested-With",
                "application/json",
                "RefreshToken"
        ));

        // 자격 증명 포함 설정
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }



}
