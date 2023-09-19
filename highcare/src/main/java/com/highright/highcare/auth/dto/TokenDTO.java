package com.highright.highcare.auth.dto;

import lombok.*;

/* 토큰 정보 담을 객체*/
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class TokenDTO {

    private String grantType;
    private String accessToken;
//    private RefreshTokenDTO refreshToken;
    private long accessTokenExpiresIn;
    private long refreshTokenExpiresIn;

    private int empNo;
    private String memberName;
    private String deptName;
    private String jobName;
    private String role;
    private String isTempPwd;

    private AccountDTO accountDTO;

}
