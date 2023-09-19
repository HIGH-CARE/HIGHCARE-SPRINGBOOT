package com.highright.highcare.approval.dto;

import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvFileDTO {

    private Long fileNo;
    private String originalFileName;
    private String savedFileName;
    private String fileUrl;
    private String apvNo;

}
