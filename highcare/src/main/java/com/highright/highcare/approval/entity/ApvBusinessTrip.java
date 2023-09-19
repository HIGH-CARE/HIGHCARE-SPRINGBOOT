package com.highright.highcare.approval.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "TBL_APV_BUSINESS_TRIP")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(
        name = "SEQ_APV_ITEMS01",
        sequenceName = "SEQ_APV_BT_ITEMS",
        initialValue = 1, allocationSize = 1
)
public class ApvBusinessTrip {
    @Id
    @Column(name = "ITEMS_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_APV_ITEMS01"
    )
    private Long itemsNo;

    @Column(name = "BT_PURPOSE")
    private String purpose;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "START_TIME")
    private String startTime;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "END_TIME")
    private String endTime;

    @Column(name = "BT_LOCATION")
    private String location;

    @Column(name = "TRIP_ATTENDEES")
    private String tripAttendees;
    @Column(name = "APV_NO")
    private Long apvNo;
}
