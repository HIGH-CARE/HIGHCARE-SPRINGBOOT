package com.highright.highcare.admin.dto;

import com.highright.highcare.auth.entity.AUTHDepartment;
import com.highright.highcare.auth.entity.AUTHJob;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
public class ADMEmployeeDTO {


    private int empNo;

    private String name;

    private AUTHDepartment deptCode;

    private String phone;

    private String email;

    private AUTHJob jobCode;

    @Builder
    public ADMEmployeeDTO(int empNo, String name, AUTHDepartment deptCode, String phone, String email, AUTHJob jobCode) {
        this.empNo = empNo;
        this.name = name;
        this.deptCode = deptCode;
        this.phone = phone;
        this.email = email;
        this.jobCode = jobCode;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empNo=" + empNo +
                ", name='" + name + '\'' +
                ", deptCode=" + deptCode +
                ", jobCode=" + jobCode +
                '}';
    }
}