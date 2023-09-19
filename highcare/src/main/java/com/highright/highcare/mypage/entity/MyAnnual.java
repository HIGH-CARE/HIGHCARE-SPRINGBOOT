package com.highright.highcare.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TBL_ANNUAL")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
        name = "MYANNUAL_SEQ_NO",
        sequenceName = "DEPARTMENT_SEQ_NO",
        initialValue = 1, allocationSize = 1
)
@Setter
@ToString
public class MyAnnual {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MYANNUAL_SEQ_NO")
    @Column(name = "EMP_NO")
    private int empNo;

    @Column(name ="BASIC_ANNUAL")
    private int bAn;
    @Column(name ="USE_ANNUAL")
    private int useAn;
    @Column(name ="ADD_ANNUAL")
    private int addAn;
    @Column(name ="TOTAL_ANNUAL")
    private int totalAn;

    @Id
    @Column(name = "ANN_NO")
    private int annNo;

    @Column(name="APV_NO")
    private String  apvNo;


    @OneToOne
    @JoinColumn(name = "APV_NO", insertable = false, updatable = false, referencedColumnName = "APV_NO")
    private MyApvVation myApvVation;
}

