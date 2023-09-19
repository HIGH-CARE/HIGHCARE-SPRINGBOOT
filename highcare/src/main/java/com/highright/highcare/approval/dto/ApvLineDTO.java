package com.highright.highcare.approval.dto;

import com.highright.highcare.approval.entity.ApvEmployee;
import com.highright.highcare.pm.dto.PmEmployeeDTO;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ApvLineDTO {

    private Long apvLineNo;
    private int degree;
    private String isApproval;
    private Date apvDate;
    private String isReference;
    private Long apvNo;

    private int empNo;

    private ApvEmployee apvEmployee;
    private String empName;
    private String jobName;
    private String deptName;
    public void getEmployeeDTO() {
        if (apvEmployee != null) {
            this.empName = apvEmployee.getName();
            this.deptName = apvEmployee.getDeptCode().getDeptName();
            this.jobName = apvEmployee.getJobCode().getJobName();
        }
    }
    }
