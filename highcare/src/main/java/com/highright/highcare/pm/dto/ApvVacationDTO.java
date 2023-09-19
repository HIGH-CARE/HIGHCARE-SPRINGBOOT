package com.highright.highcare.pm.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvVacationDTO {

    private String startDate;
    private String endDate;
    private String type;
    private String apvComment;
    private String itemsNo;
    private String apvNo;
    private String offTypeOne;
    private String offTypeTwo;
    private Integer amount;

//    private List<ApvPmFormDTO> pmForms;

}
