package com.highright.highcare.approval.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvBusinessTripDTO {

    private Long itemsNo;
    private String purpose;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String location;
    private String tripAttendees;
    private String refApvNo;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;
}
