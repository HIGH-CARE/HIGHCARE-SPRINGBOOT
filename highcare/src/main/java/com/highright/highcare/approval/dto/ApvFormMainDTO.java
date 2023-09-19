package com.highright.highcare.approval.dto;

import com.highright.highcare.approval.entity.ApvEmployee;
import lombok.*;

import java.sql.Date;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
//@ToString
public class ApvFormMainDTO {

    private Long apvNo;
    private String title;
    private Date writeDate;
    private String apvStatus;
    private String isUrgency;
    private String category;
    private String contents1;
    private String contents2;
    private String totalAmount;
    private Long refApvNo;
    private int empNo;

    private ApvEmployee apvEmployee;
    private String empName;
    private String deptName;
    private String jobName;

    public void getEmployeeDTO() {
        if (apvEmployee != null) {
            this.empName = apvEmployee.getName();
            this.deptName = apvEmployee.getDeptCode().getDeptName();
            this.jobName = apvEmployee.getJobCode().getJobName();
        }
    }

    @Override
    public String toString() {
        return "ApvFormMainDTO{" +
                "apvNo=" + apvNo +
                ", title='" + title + '\'' +
                ", writeDate=" + writeDate +
                ", apvStatus='" + apvStatus + '\'' +
                ", isUrgency='" + isUrgency + '\'' +
                ", category='" + category + '\'' +
                ", contents1='" + contents1 + '\'' +
                ", contents2='" + contents2 + '\'' +
                ", empNo=" + empNo +
                ", empName='" + empName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }
}
