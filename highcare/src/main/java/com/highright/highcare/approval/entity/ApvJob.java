package com.highright.highcare.approval.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_JOB")
@Getter
public class ApvJob {

    @Id
    @Column(name="JOB_CODE")
    private int jobCode;

    @Column(name="JOB_NAME")
    private String jobName;

    @Override
    public String toString() {
        return "ApvJob{" +
                "ApvjobCode=" + jobCode +
                ", ApvjobName='" + jobName + '\'' +
                '}';
    }
}
