package com.highright.highcare.auth.service;

import com.highright.highcare.auth.dto.AccountDTO;
import com.highright.highcare.auth.dto.LoginMemberDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AuthService {

    Object selectLogin(LoginMemberDTO loginMemberDTO, HttpServletResponse response);

    Object reIssueToken(HttpServletRequest request);

//    Object insertOauthJwt(Map<String, Object> data, HttpServletResponse response);

    Object insertOauthRegist(Map<String, Object> data, HttpServletResponse response);


    Object updateAndInsertPassword(AccountDTO accountDTO);
}
