package com.highright.highcare.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "TBL_CALENDAR")
@Getter
@Setter
@ToString
public class MyCalendar {
    @Id
    @Column(name = "CALENDAR_CODE")
    private int code;
    @Column(name = "CALENDAR_CONTENTS")
    private String contents;
    @Column(name = "CALENDER_DATE")
    private Date date;
    @Column(name = "START_TIME")
    private String startTime;
    @Column(name = "END_TIME")
    private String endTime;

    @ManyToOne
    @JoinColumn(name = "EMP_NO")
    private MyEmployee myEmp;

}
