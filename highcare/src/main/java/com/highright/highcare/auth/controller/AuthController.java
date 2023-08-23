package com.highright.highcare.auth.controller;


import com.highright.highcare.auth.dto.LoginMemberDTO;
import com.highright.highcare.auth.service.AuthService;
import com.highright.highcare.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> selectLogin(@RequestBody LoginMemberDTO loginMemberDTO
                                            , HttpServletResponse response){
        log.info("[AuthController] Login : loginMemberDTO ==== {}", loginMemberDTO);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "로그인 성공", authService.selectLogin(loginMemberDTO, response)));
    }

    @GetMapping("/reissue")
    public ResponseEntity<ResponseDTO> updateReissue(HttpServletRequest request, @RequestParam String id){
        log.info("[AuthController] Reissue ===== {}", "컨트롤러 접근");
        log.info("[AuthController] Reissue ===== id {}", id);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "엑세스토큰 재발급 성공", authService.reIssueToken(request)));
    }





}
