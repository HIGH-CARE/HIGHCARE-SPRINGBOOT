package com.highright.highcare.approval.dto;

import lombok.*;

import javax.persistence.Column;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvFamilyEventDTO {

    private Long itemsNo;
    private String type;
    private Date familyDate;
    private String bank;
    private String accountHolder;
    private String accountNumber;
    private int payment;
    private char isSendingWreath;
    private String sendingName;
    private String sendingAddress;
    private String sendingPhone;
    private String requestsNote;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;
}
