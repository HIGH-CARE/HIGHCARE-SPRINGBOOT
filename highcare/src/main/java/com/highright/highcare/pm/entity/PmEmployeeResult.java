package com.highright.highcare.pm.entity;

import lombok.Getter;

@Getter
public class PmEmployeeResult {

    private int empNo;
    private String name;
    private String email;
    private String phone;
    private String title;

    public PmEmployeeResult(final PmEmployee employees) {
        this.empNo = employees.getEmpNo();
        this.name = employees.getEmpName();
        this.email = employees.getEmpEmail();
        this.phone = employees.getPhone();
        this.title = employees.getJob().getName();
//        this.title = employees.getJob().getJobName();
    }
}