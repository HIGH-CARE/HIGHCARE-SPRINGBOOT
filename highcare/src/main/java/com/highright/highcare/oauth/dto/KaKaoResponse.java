package com.highright.highcare.oauth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KaKaoResponse {

    private String code;
    private String state="";
    private String browser;
    private String device;
}
