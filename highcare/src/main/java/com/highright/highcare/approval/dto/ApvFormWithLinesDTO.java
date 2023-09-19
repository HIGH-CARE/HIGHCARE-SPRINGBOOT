package com.highright.highcare.approval.dto;

import lombok.*;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvFormWithLinesDTO {


    private ApvFormDTO apvFormDTO;
    private List<ApvLineDTO> apvLineDTOs;
}
