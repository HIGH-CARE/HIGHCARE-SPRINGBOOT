package com.highright.highcare.oauth.controller;

import com.highright.highcare.auth.service.AuthService;
import com.highright.highcare.common.ResponseDTO;
import com.highright.highcare.oauth.dto.KaKaoResponse;
import com.highright.highcare.oauth.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final AuthService authService;

    private final OAuthService oAuthService;

    // (구글) 소셜로그인 연동 신청
    @Operation(summary = "소셜로그인 계정연동 요청", description = "회원의 소셜로그인 계정과 연동하는 요청이 진행됩니다.", tags = {"OAuthController"})
    @PostMapping("/api/oauth/regist")
    public ResponseEntity<ResponseDTO> insertOauthRegist(@RequestBody Map<String, Object> data, HttpServletResponse response){
        log.info("[OAuthController] insertOauthRegist data====  {} ", data);



        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "소셜로그인 연동 확인", authService.insertOauthRegist(data, response)));
    }

    // 카카오 소셜로그인 요청
    @Operation(summary = "소셜로그인 요청", description = "소셜로그인 요청이 진행됩니다.", tags = {"OAuthController"})
    @GetMapping("/api/oauth/kakao")
    public ResponseEntity<ResponseDTO> selectAuthorize(@ModelAttribute KaKaoResponse kaKaoResponse, HttpServletResponse response){

        log.info("=========================kakao controller====kaKaoResponse {}", kaKaoResponse);

        log.info("=========================kakao controller===============code : {}", kaKaoResponse.getCode());
        log.info("=========================kakao controller===============state : id : {}",kaKaoResponse.getState());
        // 1. 서버 토큰 받기
        String accessToken = (String) oAuthService.tokenRequest(kaKaoResponse.getCode());

        System.out.println("accessToken = " + accessToken);

        // 2. 토큰으로 클라이언트(사용자) 정보 받아오기
        HashMap<String, Object> userInfo = oAuthService.getUserInfo(accessToken, kaKaoResponse.getState());

        System.out.println("###access_Token#### : " + accessToken);
        System.out.println("userInfo = " + userInfo);
        System.out.println("###nickname#### : " + userInfo.get("nickname"));
        System.out.println("###email#### : " + userInfo.get("email"));
        System.out.println();

        userInfo.put("browser", kaKaoResponse.getBrowser());
        userInfo.put("device", kaKaoResponse.getDevice());

        // 3. 사용자 정보(userInfo)를 바탕으로 db 입력, 서버전용 jwt 토큰 발급, 로그인 인가 처리
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "소셜로그인 연동 확인",authService.insertOauthRegist(userInfo, response)));
    }



}
