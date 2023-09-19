package com.highright.highcare.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="TBL_MY_WORK_LOG")
@Getter
@Setter
public class MyWorklog {

    @Id
    @Column(name = "WORK_CODE")
    private int code;
    @Column(name = "W_DATE")
    private Date wDate;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "WORK_CONTENT")
    private String content;
    @Column(name = "START_DATE")
    private Date sDate;
    @Column(name = "END_DATE")
    private Date eDate;

    @ManyToOne
    @JoinColumn(name="EMP_NO")
    private MyEmployee myEmp;

}
