package com.highright.highcare.pm.dto;


import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvPmFormDTO {

    private String apvNo;
    private String title;
    private Timestamp writeDate;
    private String apvStatus;
    private char iSurGenCy;
    private String apvCategory;
    private String contentsOne;
    private String contentsWwo;
    private Integer empNo;
    private String mainTitle;
    private List<ApvVacationDTO> vacation;

}
