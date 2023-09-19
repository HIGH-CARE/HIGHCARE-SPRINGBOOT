package com.highright.highcare.mypage.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class MyManegementDTO {

    private int manNo;
    private String sTime;
    private String eTime;
    private String manTime;
    private String status;
    private int empNo;
    private String workDate;
    private String tWorkDate;

}
