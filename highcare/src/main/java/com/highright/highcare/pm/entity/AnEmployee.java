package com.highright.highcare.pm.entity;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name="TBL_EMPLOYEE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@DynamicInsert
public class AnEmployee {

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

    @Column(name = "DEPT_CODE")
    private int deptCode;

    @Column(name = "JOB_CODE")
    private int jobcode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EDUCATION")
    private String education;

    @Column(name = "TELEPHONE")
    private String telephone;

    @ManyToOne
    @JoinColumn(name="JOB_CODE", insertable = false, updatable = false)
    private PmJob job;


    @ManyToOne
    @JoinColumn(name = "DEPT_CODE",insertable = false, updatable = false )
    private PmDepartment dt;

//    @OneToMany
//    @JoinColumn(name = "EMP_NO", insertable = false, updatable = false)
//    private List<AnAnual> anAnual;
    // 개인 연차 조회시 얘가 계속 참조되서 오류발생
    // 이문제를 해결하기 위해서는...또 엔티티를 갈라야하는지..
}
