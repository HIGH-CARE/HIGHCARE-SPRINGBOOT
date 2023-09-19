package com.highright.highcare.mypage.dto;

import com.highright.highcare.mypage.entity.Department;
import com.highright.highcare.mypage.entity.Job;
import com.highright.highcare.mypage.entity.MyProfile;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyEmployeeDTO {

    private int empNo;
    private String name;
    private String email;
    private String phone;
    private String reNo;
    private String sDate;
    private DepartmentDTO dep;
    private JobDTO job;
    private String address;
    private String tel;

    private Department deptName;

    private Job jobName;

    private List<MyAnnualDTO> myAnnual;


    private List<MyManegementDTO> manegementList;
}

