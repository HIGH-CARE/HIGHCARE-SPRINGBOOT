package com.highright.highcare.pm.dto;

import com.highright.highcare.approval.dto.ApvLineDTO;
import com.highright.highcare.pm.entity.AnAnual;
import com.highright.highcare.pm.entity.Career;
import com.highright.highcare.pm.entity.Certification;
import com.highright.highcare.pm.entity.Military;
import lombok.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PmEmployeeDTO {

    private int empNo;
    private String empName;
    private String empEmail;
    private String phone;
    private String residentNo;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private char isResignation;
    private int deptCode;
    private int jobCode;
    private String address;
    private String education;
    private String telephone;
    private DepartmentDTO dt;
    private JobDTO job;
//    private MilitaryDTO military;
//    private CareerDTO career;
//    private CertificationDTO certification;
    private List<MilitaryDTO> military;
    private List<CareerDTO> career;
    private List<CertificationDTO> certification;


    private AnAnual anAnual;
}
