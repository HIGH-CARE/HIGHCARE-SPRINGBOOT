package com.highright.highcare.pm.entity;


import lombok.Getter;

import java.sql.Date;

@Getter
public class PmTotalResult {

    private String empName;
    private String residentNo;
    private String phone;
//    private String birthday;
    private String address;
    private String email;
    private String education;
    private String milStatus;
    private Integer milNo;
    private char isWhether;
    private Integer carNo;
//    private String carName;
//    private int history;
//    private Date carStartDate;
//    private Date carEndDate;
//    private String carjob;
    private Integer cerNo;
//    private Date cerStartDate;
//    private Date cerEndDate;
//    private String institution;
//    private String cerName;

    public PmTotalResult (final PmEmployee pmTotalEmp, Career career, Certification certification, Military military) {
        this.empName = pmTotalEmp.getEmpName();
        this.residentNo = pmTotalEmp.getResidentNo();
        this.phone = pmTotalEmp.getPhone();
        this.address = pmTotalEmp.getAddress();
        this.email = pmTotalEmp.getEmpEmail();
        this.education = pmTotalEmp.getEducation();
        this.milStatus = military.getStatus();
        this.milNo = military.getMilNo();
        this.isWhether = military.getIsWhether();
        this.carNo = career.getCarNo();
        this.cerNo = certification.getCerNo();


//        this.milNo = pmTotalEmp.getMilNo().getMilNo();
//        this.isWhether = pmTotalEmp.getMilNo().getIsWhether();
//        this.carNo = pmTotalEmp.getCareers().getCarNo();
//        this.carName = pmTotalEmp.getCareers().getName();
//        this.history = pmTotalEmp.getCareers().getHistory();
//        this.carStartDate = pmTotalEmp.getCareers().getStartDate();
//        this.carEndDate = pmTotalEmp.getCareers().getEndDate();
//        this.carjob = pmTotalEmp.getCareers().getJob();
//        this.cerNo = pmTotalEmp.getCertifications().getCerNo();
//        this.cerStartDate = pmTotalEmp.getCertifications().getStartDate();
//        this.cerEndDate = pmTotalEmp.getCertifications().getEndDate();
//        this.institution = pmTotalEmp.getCertifications().getInstitution();
//        this.cerName = pmTotalEmp.getCertifications().getName();


//        this.milStatus = pmTotalEmp.getMilNo().getStatus();
//        this.milNo = pmTotalEmp.getMilNo().getMilNo();
//        this.isWhether = pmTotalEmp.getMilNo().getIsWhether();
//        this.carNo = pmTotalEmp.getCareers().getCarNo();
//        this.carName = pmTotalEmp.getCareers().getName();
//        this.history = pmTotalEmp.getCareers().getHistory();
//        this.carStartDate = pmTotalEmp.getCareers().getStartDate();
//        this.carEndDate = pmTotalEmp.getCareers().getEndDate();
//        this.carjob = pmTotalEmp.getCareers().getJob();
//        this.cerNo = pmTotalEmp.getCertifications().getCerNo();
//        this.cerStartDate = pmTotalEmp.getCertifications().getStartDate();
//        this.cerEndDate = pmTotalEmp.getCertifications().getEndDate();
//        this.institution = pmTotalEmp.getCertifications().getInstitution();
//        this.cerName = pmTotalEmp.getCertifications().getName();


    }
}
