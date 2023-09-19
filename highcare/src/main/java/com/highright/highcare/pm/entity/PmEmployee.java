package com.highright.highcare.pm.entity;


import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name="TBL_EMPLOYEE")
@SequenceGenerator(
        name="EMPLOYEE_SEQ_GENERATOR",
        sequenceName = "SEQ_EMPLOYEE_CODE",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@NamedEntityGraph(name = "employees-with-related-entities",
//        attributeNodes = {
//                @NamedAttributeNode("military")
//        })
public class PmEmployee {
    @Id
    @Column(name = "EMP_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "EMPLOYEE_SEQ_GENERATOR"
    )
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

    @Column(name = "END_DATE", nullable = true)
    private Date endDate;

    @Column(name = "IS_RESIGNATION")
    private char isResignation;

    @Column(name = "DEPT_CODE")
    private int deptCode;

    @Column(name = "JOB_CODE")
    private int jobCode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EDUCATION")
    private String education;

    @Column(name = "TELEPHONE")
    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="JOB_CODE", insertable = false, updatable = false)
    private PmJob job;

    @OneToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    private List<Military> military;

    @OneToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    private List<Career> career;

    @OneToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    private List<Certification> certification;

    @ManyToOne
    @JoinColumn(name="JOB_CODE", insertable = false, updatable = false )
    private PmDepartment dt;

    //    @Column(name="JOB_CODE")
    //    private Integer job;




}


