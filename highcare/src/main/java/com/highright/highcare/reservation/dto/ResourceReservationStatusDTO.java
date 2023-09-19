package com.highright.highcare.reservation.dto;

import com.highright.highcare.bulletin.dto.BulletinEmployeeDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResourceReservationStatusDTO {
    private int statusCode;
    private String reservationStatus;
    private String startTime;
    private String endTime;
    private String reason;
//    private String reservationDate;
private java.sql.Date  reservationDate;

    private BulletinEmployeeDTO bulletinEmployee;
    private ResourceDTO resource;

    private int empNo;
    private int resourceCode;

}
