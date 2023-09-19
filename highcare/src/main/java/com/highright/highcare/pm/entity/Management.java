package com.highright.highcare.pm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="TBL_MANAGEMENT")
@SequenceGenerator(
        name="MANAGEMENT_SEQ_GENERATOR",
        sequenceName = "SEQ_MANAGEMENT_CODE",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Management {

    @Id
    @Column(name = "MAN_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MANAGEMENT_SEQ_GENERATOR"
    )
    private Long manNo;

    @Column(name = "START_TIME")
    private String startTime;

    @Column(name = "END_TIME")
    private String endTime;

    @Column(name = "MAN_DATE")
    private String manDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "WORK_TIME")
    private String workTime;

    @Column(name = "TOTAL_WORK_TIME")
    private String totalWorkTime;

    @Column(name = "EMP_NO")
    private int empNo;

    @ManyToOne
    @JoinColumn(name = "EMP_NO", insertable = false, updatable = false)
    private MgEmployee mgEmployee;

    @Override
    public String toString() {
        return "Management{" +
                "manNo=" + manNo +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", manDate='" + manDate + '\'' +
                ", status='" + status + '\'' +
                ", workTime='" + workTime + '\'' +
                ", totalWorkTime='" + totalWorkTime + '\'' +
                ", empNo=" + empNo +
                ", mgEmployee=" + mgEmployee +
                '}';
    }


    //    @Column(name = "EMP_NO")
//    private int empNo;
}
