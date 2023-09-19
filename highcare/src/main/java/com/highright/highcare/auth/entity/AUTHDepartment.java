package com.highright.highcare.auth.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_DEPARTMENT")
@Getter
public class AUTHDepartment {

    @Id
    @Column(name="DEPT_CODE")
    private int deptCode;

    @Column(name="NAME")
    private String deptName;

    @Override
    public String toString() {
        return "Department{" +
                "deptCode=" + deptCode +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
