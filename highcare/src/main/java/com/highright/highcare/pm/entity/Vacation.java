package com.highright.highcare.pm.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "TBL_APV_VACATION")
@Getter
@SequenceGenerator(
        name = "MyAPV_VACATION_NO",
        sequenceName = "APV_VACATION_NO",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class Vacation implements Serializable {

    @Id
    @Column(name="ITEMS_NO")
    private String ItemNo;

    @Column(name="APV_NO")
    private String apvNo;      // fk

    @Column(name="TYPE")
    private String type;    // 휴가종류 : 반차, 연차

    @Column(name="START_DATE")
    private Date sdate;

    @Column(name="END_DATE")
    private Date edate;

    @Column(name="APV_COMMENT")
    private String comment;

    @Column(name="OFFTYPE1")
    private String off1;    // 반차

    @Column(name="OFFTYPE2")
    private String off2;    // 반차끝
}
