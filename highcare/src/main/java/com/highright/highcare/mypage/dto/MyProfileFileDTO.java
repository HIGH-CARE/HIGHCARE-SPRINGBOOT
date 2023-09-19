package com.highright.highcare.mypage.dto;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyProfileFileDTO {

    private int photoCode;
    private int code;
    private String name;
    private String chName;
    private Date date;
    private String profileImgUrl;
}
