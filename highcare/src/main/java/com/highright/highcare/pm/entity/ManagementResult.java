package com.highright.highcare.pm.entity;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class ManagementResult {
    private Long manNo;
    private String empName; //사원이름
    private String startTime; // 출근시간
    private String endTime; // 퇴근시간
    private String status; // 상태 (출근 or 퇴근)
    private String workTime; // 근무시간
    private String totalWorkTime; // 총 근무시간
    private String deptName; // 부서 이름
    private String jobName; // 직급 이름
    private int empNo;
    private String manDate;
    public ManagementResult(Management management) {
        this.manNo = management.getManNo();
        this.empName = management.getMgEmployee().getEmpName();
        this.startTime = management.getStartTime();
        this.endTime = management.getEndTime();
        this.status = management.getStatus();
        this.workTime = management.getWorkTime();
        this.totalWorkTime = management.getTotalWorkTime();
        this.deptName = management.getMgEmployee().getReDepartment().getName();
        this.jobName = management.getMgEmployee().getReJob().getJobName();
        this.empNo = management.getMgEmployee().getEmpNo();
        this.manDate = management.getManDate();

    }




//    Long datetime = System.currentTimeMillis();
//    Timestamp timestamp = new Timestamp(datetime);
//
////        System.out.println("Datetime = " + datetime);
////        System.out.println("Timestamp: "+timestamp);

}
