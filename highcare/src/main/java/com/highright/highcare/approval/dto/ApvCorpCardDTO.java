package com.highright.highcare.approval.dto;

import lombok.*;

import javax.persistence.Column;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvCorpCardDTO {

    private Long itemsNo;
    private String cardNo;
    private int paymentMonth;
    private String details;
    private String account;
    private int amount;
    private String cardComment;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;
}
