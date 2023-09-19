package com.highright.highcare.approval.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvOfficialDTO {

    private Long itemsNo;
    private String officialTitle;
    private String reception;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;
}
