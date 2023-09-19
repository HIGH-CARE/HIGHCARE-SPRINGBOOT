package com.highright.highcare.reservation.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TBL_RESOURCE")
@SequenceGenerator(
        name = "RES_SEQ_GENERATOR",
        sequenceName = "SEQ_RESOURCE_CODE",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Resource {
    @Id
    @Column(name = "RESOURCE_CODE")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "RES_SEQ_GENERATOR"
    )
    private int resourceCode;
    @Column(name = "RESOURCE_NAME")
    private String resourceName;
    @Column(name = "AREA")
    private String area;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "SERVICE_GUIDE")
    private String serviceGuide;
    @Column(name = "CREATION_DATE")
    private java.sql.Date creationDate;
    @Column(name = "MODIFIED_DATE")
    private java.sql.Date modifiedDate;
    @Column(name = "DELETE_YN")
    private char deleteYn;
    @Column(name = "START_TIME")
    private String startTime;
    @Column(name = "END_TIME")
    private String endTime;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_CODE")
    private ResourceCategory resourceCategory;
}
