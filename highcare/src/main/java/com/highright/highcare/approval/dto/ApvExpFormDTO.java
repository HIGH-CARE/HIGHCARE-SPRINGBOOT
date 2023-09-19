package com.highright.highcare.approval.dto;

import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvExpFormDTO {

    private Long itemsNo;
    private Date requestDate;
    private String payee;
    private String bank;
    private String accountHolder;
    private String accountNumber;
    private String details;
    private String account;
    private int amount;
    private String comment;
    private Long refApvNo;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;
}
