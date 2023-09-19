package com.highright.highcare.auth.controller;


import com.highright.highcare.auth.dto.AccountDTO;
import com.highright.highcare.auth.dto.LoginMemberDTO;
import com.highright.highcare.auth.service.AuthService;
import com.highright.highcare.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인 요청", description = "로그인 및 인증이 진행됩니다.", tags = {"AuthController"})
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> selectLogin(@RequestBody LoginMemberDTO loginMemberDTO
                                            , HttpServletResponse response){
        log.info("[AuthController] Login : loginMemberDTO ==== {}", loginMemberDTO);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "로그인", authService.selectLogin(loginMemberDTO, response)));
    }

    @Operation(summary = "jwt 엑세스토큰 재발급 요청", description = "엑세스토큰 재발급 요청이 진행됩니다.", tags = {"AuthController"})
    @GetMapping("/reissue")
    public ResponseEntity<ResponseDTO> updateReissue(HttpServletRequest request, @RequestParam String id){
        log.info("[AuthController] Reissue ===== {}", "컨트롤러 접근");
        log.info("[AuthController] Reissue ===== id {}", id);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "엑세스토큰 재발급", authService.reIssueToken(request)));
    }
    @Operation(summary = "비밀번호 변경 요청", description = "회원 비밀번호 변경 요청이 진행됩니다.", tags = {"AuthController"})
    @PostMapping("/password")
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody @Valid AccountDTO accountDTO){

        log.info("updatePassoword start ============{}", accountDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "비밀번호 변경", authService.updateAndInsertPassword(accountDTO)));


    }





}
