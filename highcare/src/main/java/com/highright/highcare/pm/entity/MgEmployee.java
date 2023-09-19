package com.highright.highcare.pm.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="TBL_EMPLOYEE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
public class MgEmployee {
    @Id
    @Column(name = "EMP_NO")
    private int empNo;

    @Column(name = "EMP_NAME")
    private String empName;

    @Column(name = "EMAIL")
    private String empEmail;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "RESIDENT_NO")
    private String residentNo;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "IS_RESIGNATION")
    private char isResignation;

//    @Column(name = "DEPT_CODE")
//    private int deptCode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EDUCATION")
    private String education;

    @Column(name = "TELEPHONE")
    private String telephone;

    @ManyToOne
    @JoinColumn(name="JOB_CODE")
    private ReJob reJob;

    @ManyToOne
    @JoinColumn(name="DEPT_CODE")
    private PmDepartment reDepartment;

    @OneToMany
    @JoinColumn(name = "MAN_NO")
    private List<Management> manNo;

//    @OneToMany
//    @JoinColumn(name = "EMP_NO")
//    private List<AnAnual> anuals;


}
