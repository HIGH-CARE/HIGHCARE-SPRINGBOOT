package com.highright.highcare.pm.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
public class AnAnualResult {

    private int empNo;
    private int annNo;
    private String apvNo;
    private int basicAnnual;
    private int useAnnual;
    private int addAnnual;
    private int totalAnnual;
    private String reason;
//    private String deptName;
//    private Date startDate;
//    private String empName;
//    private String jobName;


    public AnAnualResult(AnAnual anAnual) {
        this.empNo = anAnual.getEmpNo();
        this.annNo = anAnual.getAnnNo();
        this.apvNo = anAnual.getApvNo();
        this.basicAnnual = anAnual.getBasicAnnual();
        this.useAnnual = anAnual.getUseAnnual();
        this.addAnnual = anAnual.getAddAnnual();
        this.totalAnnual = anAnual.getTotalAnnual();
        this.reason = anAnual.getReason();
//        this.deptName = anAnual.getAnEmployee().getDepartment().getName();
//        this.startDate = anAnual.getAnEmployee().getStartDate();
//        this.empName = anAnual.getAnEmployee().getEmpName();
//        this.jobName = anAnual.getAnEmployee().getJob().getName();

    }


}
