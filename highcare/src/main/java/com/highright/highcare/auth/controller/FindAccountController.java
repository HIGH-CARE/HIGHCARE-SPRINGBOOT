package com.highright.highcare.auth.controller;

import com.highright.highcare.auth.dto.AUTHFindAccountDTO;
import com.highright.highcare.auth.dto.MailDTO;
import com.highright.highcare.auth.service.FindAccountService;
import com.highright.highcare.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@Slf4j
@RestController
@RequestMapping("/api/auth/find")
@RequiredArgsConstructor
public class FindAccountController {

    private final FindAccountService findAccountService;

    @Operation(summary = "이메일 본인인증 요청", description = "회원 본인 인증코드 발송용 메일발송 요청이 진행됩니다..", tags = {"FindAccountController"})
    @PostMapping("/mail")
    public ResponseEntity<ResponseDTO> emailSendAuth(@RequestBody MailDTO mailDTO){
        log.info("FindAccountController execMail :==={}", mailDTO);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "이메일 인증 요청", findAccountService.emailSendAuth(mailDTO)));
    }

//    @PostMapping("/phone")
//    public ResponseEntity<ResponseDTO> phoneSendAuth(@ModelAttribute {
//
//    }

    @Operation(summary = "본인 인증코드 확인 요청", description = "본인 인증코드 확인 작업이 진행됩니다.", tags = {"FindAccountController"})
    @PostMapping("/authcheck")
    public ResponseEntity<ResponseDTO> selectAuthNumberCheck(@RequestBody AUTHFindAccountDTO authFindAccountDTO){
        log.info("FindAccountController authFindAccountDTO :==={}", authFindAccountDTO);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "인증번호 check", findAccountService.selectAuthNumberCheck(authFindAccountDTO)));
    }

}
