package com.highright.highcare.approval.entity;

import com.highright.highcare.pm.entity.PmEmployee;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_APV_FORM")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name = "APV_SEQ_NO",
        sequenceName = "SEQ_APV_NO",
        initialValue = 1, allocationSize = 1
)
public class ApvFormMain {

    @Id
    @Column(name = "APV_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "APV_SEQ_NO"
    )
    private Long apvNo;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "WRITE_DATE")
    private Date writeDate;

    @Column(name = "APV_STATUS")
    private String apvStatus;

    @Column(name = "ISURGENCY")
    private String isUrgency;

    @Column(name = "APV_CATEGORY")
    private String category;

    @Column(name = "CONTENTS1")
    private String contents1;

    @Column(name = "CONTENTS2")
    private String contents2;

    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;

    @Column(name = "REF_APV_NO")
    private Long refApvNo;

    @Column(name = "EMP_NO")
    private int empNo;

    @Transient
    private String empName;

    @Transient
    private String deptName;

    @Transient
    private String jobName;




    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("apvLineNo ASC")
    @JoinColumn(name = "APV_NO")
    private List<ApvLine> apvLines = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "APV_NO")
    private List<ApvFile> apvFiles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "EMP_NO", updatable = false, insertable = false)
    private ApvEmployee apvEmployee;


    public void getEmployee() {
        if (apvEmployee != null) {
            this.empName = apvEmployee.getName();
            this.deptName = apvEmployee.getDeptCode().getDeptName();
            this.jobName = apvEmployee.getJobCode().getJobName();
            this.apvLines = apvLines;
        }
    }



    @Override
    public String toString() {
        return "ApvFormMain{" +
                "apvNo=" + apvNo +
                ", title='" + title +
                ", writeDate=" + writeDate +
                ", apvStatus='" + apvStatus +
                ", isUrgency='" + isUrgency +
                ", category='" + category +
                ", contents1='" + contents1 +
                ", contents2='" + contents2 +
                ", empNo=" + empNo +
                ", empName=" + empName +
                ", deptName=" + deptName +
                ", jobName=" + jobName +
                ", apvLines=" + apvLines +
//                ", apvEmployee=" + apvEmployee +
                '\'';
    }
}
