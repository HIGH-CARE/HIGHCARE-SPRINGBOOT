package com.highright.highcare.pm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="TBL_JOB")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReJob {

    @Id
    @Column(name="JOB_CODE")
    private int reJobCode;

    @Column(name="JOB_NAME")
    private String jobName;

    @Column(name = "UPPER_JOB_CODE", nullable = true)
    private Integer upperJobCode;

    @Column(name = "UPPER_JOB_NAME")
    private String upperJobName;


}
