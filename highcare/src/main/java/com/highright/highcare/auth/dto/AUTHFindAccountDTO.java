package com.highright.highcare.auth.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AUTHFindAccountDTO {

    private String name;
    private String id;               // phone number or email address
    private String authCode;      // 인증코드
}
