package com.highright.highcare.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="TBL_EMPLOYEE")
@Getter
@Setter
@NoArgsConstructor
public class AUTHEmployee {

    @Id
    @Column(name="EMP_NO")
    private int empNo;

    @Column(name="EMP_NAME")
    private String name;

    @OneToOne
    @JoinColumn(name="DEPT_CODE")
    private AUTHDepartment deptCode;

    @Column(name="PHONE")
    private String phone;

    @Column(name="EMAIL")
    private String email;

    @OneToOne
    @JoinColumn(name="JOB_CODE")
    private AUTHJob jobCode;

    @Builder
    public AUTHEmployee(int empNo, String name, AUTHDepartment deptCode, String phone, String email, AUTHJob jobCode) {
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