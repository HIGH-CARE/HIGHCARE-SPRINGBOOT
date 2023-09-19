package com.highright.highcare.pm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name="TBL_MILITARY")
@SequenceGenerator(
        name="MILITARY_SEQ_GENERATOR",
        sequenceName = "SEQ_MILITARY_CODE",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Military {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MILITARY_SEQ_GENERATOR"
    )
    @Column(name = "MIL_NO")
    private Integer milNo;

//    @Column(name = "EMP_NO")
//    private int empNo;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "IS_WHETHER")
    private char isWhether;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "EMP_NO")
    private PmEmployee employees;

    @Override
    public String toString() {
        return "Military{" +
                "milNo=" + milNo +
                ", status='" + status + '\'' +
                ", isWhether=" + isWhether +
//                ", employees=" + employees +
                '}';
    }
}
