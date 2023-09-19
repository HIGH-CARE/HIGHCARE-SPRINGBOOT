package com.highright.highcare.approval.entity;

import com.highright.highcare.pm.dto.PmEmployeeDTO;
import com.highright.highcare.pm.entity.PmEmployee;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "TBL_APV_LINE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
@SequenceGenerator(
        name = "SEQ_APV_LINES",
        sequenceName = "SEQ_APV_LINES",
        initialValue = 1, allocationSize = 1
)
public class ApvLine {

    @Id
    @Column(name = "APV_LINE_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_APV_LINES"
    )
    private Long apvLineNo;

    @Column(name = "DEGREE")
    private int degree;

    @Column(name = "ISAPPROVAL")
    private String isApproval;

    @Column(name = "APV_DATE")
    private Date apvDate;

    @Column(name =  "ISREFERENCE")
    private String isReference;

    @Column(name = "APV_NO")
    private Long apvNo;

    @Column(name = "EMP_NO")
    private int empNo;

    @Transient
    private String empName;

    @Transient
    private String deptName;

    @Transient
    private String jobName;

    @ManyToOne
    @JoinColumn(name = "EMP_NO", updatable = false, insertable = false)
    private ApvEmployee apvEmployee;

    public void getEmployee() {
        if (apvEmployee != null) {
            this.empName = apvEmployee.getName();
            this.deptName = apvEmployee.getDeptCode().getDeptName();
            this.jobName = apvEmployee.getJobCode().getJobName();
        }
    }

    @Override
    public String toString() {
        return "ApvLine: " +
                "apvLineNo=" + apvLineNo +
                ", degree=" + degree +
                ", isApproval='" + isApproval +
                ", apvDate='" + apvDate +
                ", apvNo=" + apvNo +
                ", apvEmployee=" + apvEmployee +
                ", empName=" + empName +
                ", deptName=" + deptName +
                ", jobName=" + jobName +
                '\'';
    }

}
