package com.highright.highcare.common;


import com.highright.highcare.admin.dto.RequestMemberDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AdminCustomBean {


    // 가입시 임시비밀번호 부여, 비밀번호찾기시 초기화 후 임시비밀번호 또는 인증번호 생성기 (기본 10자리)
    public String randomPassword (){

        return UUID.randomUUID().toString()
                .replaceAll("[\\-]", "")
                .replaceAll("[^a-zA-Z0-9]", "")
                .substring(0, 10);
    }

    // 리프레시토큰 생성기 (uuid=)
    public String randomRefreshToken(){
        return UUID.randomUUID().toString() + "=";
    }

    // 회원 아이디 생성기 ( 사원번호 + 핸드폰번호 뒷 4자리)
    public String idAccountGenerator(RequestMemberDTO reqMember){
        return reqMember.getEmpNo() + reqMember.getPhone().substring(reqMember.getPhone().length() - 4);
    }




}
