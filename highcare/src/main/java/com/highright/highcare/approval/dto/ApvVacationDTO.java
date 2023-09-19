package com.highright.highcare.approval.dto;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvVacationDTO {

    private Long itemNo;
    private String startDate;
    private String endDate;
    private String type;
    private String comment;
    private Double amount;
    private String offType1;
    private String offType2;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;
}
