package com.highright.highcare.approval.dto;

import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvIssuanceDTO {

    private Long itemsNo;
    private String type;
    private String subType;
    private String submission;
    private String uses;
    private String requests;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;
}
