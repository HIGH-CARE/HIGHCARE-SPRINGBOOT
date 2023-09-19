package com.highright.highcare.admin.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class RequestMemberDTO {

    private int empNo;
    private String Name;
    private String jobName;
    private String deptName;
    private String phone;
    private String email;


}
