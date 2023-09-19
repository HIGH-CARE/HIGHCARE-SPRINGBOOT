package com.highright.highcare.reservation.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TBL_RESOURCE_CATEGORY")
@SequenceGenerator(
        name = "RES_CATEGORY_SEQ_GENERATOR",
        sequenceName = "SEQ_RES_CATEGORY_CODE",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResourceCategory {
    @Id
    @Column(name = "CATEGORY_CODE")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "RES_CATEGORY_SEQ_GENERATOR"
    )
    private int categoryCode;
    @Column(name = "NAME")
    private String name;

}
