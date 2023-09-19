package com.highright.highcare.admin.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateAccountDTO {

    private String id;
    private String status;

    private String empNo;
    private String name;
    private String jobName;
    private String deptName;
    private String phone;
    private String email;

}
