package com.highright.highcare.mypage.dto;

import com.highright.highcare.mypage.entity.MyAnnual;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyEmployeeDTO {

    private int empNo;
//    private String name;
//    private String email;
//    private String phone;
//    private String reNo;    // 주민번호
//    private String sDate;   // 입사일
////    private String eDate;
////    private String isRes;  // 퇴사여부
//    private DepartmentDTO dep;
//    private JobDTO job;
//    private String address;
////    private String edu;
//    private String tel;    // 내선전화

    private List<MyAnnualDTO> myAnnual;

}

