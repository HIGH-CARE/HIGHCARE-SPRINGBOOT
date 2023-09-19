package com.highright.highcare.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TBL_MANAGEMENT")
@SequenceGenerator(
        name="MYMANAGEMENT_SEQ_GENERATOR",
        sequenceName = "MANAGEMENT_SEQ_GENERATOR",
        initialValue = 1, allocationSize = 1
        )
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class MyManegement {
    @Id
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "MYMANAGEMENT_SEQ_GENERATOR")
    @Column(name = "MAN_NO")
    private int manNo;

    @Column(name = "START_TIME")
    private String sTime;
    @Column(name = "END_TIME")
    private String eTime;
    @Column(name = "MAN_DATE")
    private String manTime;
    @Column(name = "STATUS")
    private String status;


    @Column(name = "EMP_NO")
    private int empNo;
    @Column(name = "WORK_TIME")
    private String workDate;
    @Column(name = "TOTAL_WORK_TIME")
    private String tWorkDate;
}
