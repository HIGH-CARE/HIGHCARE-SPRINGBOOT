package com.highright.highcare.oauth.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OAuthUserDTO {

    private String oauthId;
    private String id;
    private String provider;
    private String provideEmail;
    private String provideNickname;
    private String provideName;

}
