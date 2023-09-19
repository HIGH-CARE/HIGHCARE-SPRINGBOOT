package com.highright.highcare.pm.dto;


import lombok.*;

import javax.persistence.Embeddable;
import java.sql.Date;
import java.util.List;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTotalDTO {

    // 사원
    private int empNo;
    private String empName;
    private String empEmail;
    private String phone;
    private String residentNo;
    private Date startDate;
    private Date endDate;
    private char isResignation;
    private int deptCode;
    private int jobCode;
    private String address;
    private String education;
    private String telephone;
    private List<MilitaryDTO> military;
    private List<CareerDTO> career;
    private List<CertificationDTO> certification;

    // 부서
//    private String deptName;
//    private String tel;
//    private String upperName;
//    private int upperCode;
//
//    // 직급
//    private String jobName;
//    private Integer upperJobCode;
//    private String upperJobName;
//
//    // 자격증
//    private String cerName;
//    private Date cerStartDate;
//    private Date cerEndDate;
//    private String institution;
//    private int cerNo;
//
//    // 병역
//    private Integer milNo;
//    private String status;
//    private char isWhether;
//
//    // 경력
//    private int carNo;
//    private String carName;
//    private int history;
//    private Date carStartDate;
//    private Date carEndDate;
//    private String caJob;

}
