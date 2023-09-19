package com.highright.highcare.approval.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;


@Entity
@Table(name = "TBL_APV_VACATION")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicInsert
@SequenceGenerator(
        name = "APV_SEQ_ITEMS07",
        sequenceName = "SEQ_APV_VA_ITEMS",
        initialValue = 1, allocationSize = 1
)
public class ApvVacation {

    @Id
    @Column(name = "ITEMS_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "APV_SEQ_ITEMS07"
    )
    private Long itemNo;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "APV_COMMENT")
    private String comment;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "OFFTYPE1")
    private String offType1;

    @Column(name = "OFFTYPE2")
    private String offType2;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "APV_NO")
//    private ApvForm apvForm;

    @Column(name = "APV_NO")
    private Long apvNo;
}
