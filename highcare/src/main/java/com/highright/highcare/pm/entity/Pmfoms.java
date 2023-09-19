package com.highright.highcare.pm.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "TBL_APV_FORM")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class Pmfoms {

    @Id
    @Column(name = "APV_NO")
    private String apvNo;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "WRITE_DATE")
    private Timestamp writeDate;

    @Column(name = "APV_STATUS")
    private String apvStatus;

    @Column(name = "ISURGENCY")
    private char iSurGenCy;

    @Column(name = "APV_CATEGORY")
    private String apvCategory;

    @Column(name = "CONTENTS1")
    private String contentsOne;

    @Column(name = "CONTENTS2")
    private String contentsWwo;

    @Column(name = "EMP_NO")
    private Integer empNo;

    @Column(name = "MAIN_TITLE")
    private String mainTitle;


//    @ManyToOne
//    @JoinColumn(name = "APV_NO", insertable = false, updatable = false)
//    private ApvVacationPm apvVacationPm;
}
