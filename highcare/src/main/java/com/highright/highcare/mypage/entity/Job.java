package com.highright.highcare.mypage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "TBL_JOB")
@Getter
@Setter
@SequenceGenerator(
        name = "JOB_SEQ_NO",
        sequenceName = "JOB_SEQ_NO",
        initialValue = 1, allocationSize = 1
)
@ToString
public class Job {

    @Id
    @Column(name = "JOB_CODE")
    private int code;

    @Column(name = "JOB_NAME")
    private String name;

}
