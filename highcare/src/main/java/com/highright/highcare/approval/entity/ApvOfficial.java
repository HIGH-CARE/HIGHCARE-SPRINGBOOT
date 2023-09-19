package com.highright.highcare.approval.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TBL_APV_OFFICIAL")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(
        name = "APV_SEQ_ITEMS08",
        sequenceName = "SEQ_APV_OF_ITEMS",
        initialValue = 1, allocationSize = 1
)
public class ApvOfficial {
    @Id
    @Column(name = "ITEMS_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "APV_SEQ_ITEMS08"
    )
    private Long itemsNo;

    @Column(name = "OFFICIAL_TITLE")
    private String officialTitle;

    @Column(name = "RECEPTION")
    private String reception;

    @Column(name = "APV_NO")
    private Long apvNo;
}
