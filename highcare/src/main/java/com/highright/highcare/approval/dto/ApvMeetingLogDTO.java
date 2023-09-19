package com.highright.highcare.approval.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApvMeetingLogDTO {

    private Long itemsNo;
    private String meetingTitle;
    private Date meetingDate;
    private String location;
    private String participants;
    private Long apvNo;

//    private ApvFormDTO apvFormDTO;

}
