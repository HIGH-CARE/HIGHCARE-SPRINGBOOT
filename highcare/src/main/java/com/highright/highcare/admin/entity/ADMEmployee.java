package com.highright.highcare.admin.entity;

import com.highright.highcare.auth.entity.AUTHDepartment;
import com.highright.highcare.auth.entity.AUTHJob;
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
public class ADMEmployee {

    @Id
    @Column(name="EMP_NO", updatable = false)
    private int empNo;

    @Column(name="EMP_NAME")
    private String name;

    @Column(name="DEPT_CODE")
    private int deptCode;

    @OneToOne
    @JoinColumn(name="DEPT_CODE", insertable = false, updatable = false)
    private AUTHDepartment dept;

    @Column(name="PHONE")
    private String phone;

    @Column(name="EMAIL")
    private String email;

    @Column(name="JOB_CODE")
    private int jobCode;

    @OneToOne
    @JoinColumn(name="JOB_CODE", insertable = false, updatable = false)
    private AUTHJob job;

    @Builder
    public ADMEmployee(int empNo, String name, int deptCode, String phone, String email, int jobCode) {
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