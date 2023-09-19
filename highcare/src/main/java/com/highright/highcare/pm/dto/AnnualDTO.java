package com.highright.highcare.pm.dto;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AnnualDTO {

    private String apvNo;
    private int empNo;
    private int basicAnnual;
    private int useAnnual;
    private int addAnnual;
    private int totalAnnual;
    private String reason;

    private List<ApvPmFormDTO> pmForms;
    private ApvVacationDTO vacation;
    private List<PmEmployeeDTO> AnEmployee;
}
