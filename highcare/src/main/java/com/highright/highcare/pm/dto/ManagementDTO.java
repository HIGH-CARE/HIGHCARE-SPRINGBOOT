package com.highright.highcare.pm.dto;

import lombok.*;

import javax.persistence.Embeddable;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ManagementDTO {

    private Long manNo;
    private String startTime;
    private String endTime;
    private String manDate;
    private String status;
    private Integer empNo;

    private PmEmployeeDTO pmEmp;
}
