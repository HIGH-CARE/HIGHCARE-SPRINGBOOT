package com.highright.highcare.mypage.dto;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyApvVationDTO {

    private String itemNo;
    private String apvNo;
    private String type;
    private String sdate;
    private String edate;
    private String comment;
    private String off1;
    private String off2;


}
