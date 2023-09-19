package com.highright.highcare.approval.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_DEPARTMENT")
@Getter
public class ApvDepartment {

    @Id
    @Column(name="DEPT_CODE")
    private int deptCode;

    @Column(name="NAME")
    private String deptName;

    @Override
    public String toString() {
        return "ApvDepartment//////////" +
                "ApvDeptCode=" + deptCode +
                ", ApvDeptName='" + deptName + '\'' +
                '}';
    }
}
