package com.highright.highcare.pm.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="TBL_JOB")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PmJob {

    @Id
    @Column(name = "JOB_CODE")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "JOB_SEQ_GENERATOR"
    )
    private int jobCode;

    @Column(name="JOB_NAME")
    private String name;

    @Column(name="UPPER_JOB_CODE",nullable = true)
    private Integer upperJobCode;

    @Column(name="UPPER_JOB_NAME")
    private String upperJobName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPPER_JOB_CODE", insertable = false, updatable = false)
    private PmJob parent;

    @OneToMany(mappedBy = "parent")
    private List<PmJob> children = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "JOB_CODE", insertable = false, updatable = false  )
//    private PmEmployee employees;



}
