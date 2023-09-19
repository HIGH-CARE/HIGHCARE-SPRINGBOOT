package com.highright.highcare.pm.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


@Entity
@Table(name = "TBL_APV_VACATION")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class ApvVacationPm implements Serializable {

    @Id
    @Column(name="ITEMS_NO")
    private String itemsNo;


    @Column(name="APV_NO")
    private String apvNo;      // fk

    @Column(name="TYPE")
    private String type;    // 휴가종류 : 반차, 연차

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    @Column(name="APV_COMMENT")
    private String apvComment;

    @Column(name="OFFTYPE1")
    private String offTypeOne;    // 반차

    @Column(name="OFFTYPE2")
    private String offTypeTwo;    // 반차끝

    @Column(name= "AMOUNT")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "APV_NO", insertable = false, updatable = false)
    private Pmfoms pmForms;
}

