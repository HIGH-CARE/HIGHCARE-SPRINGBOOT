package com.highright.highcare.mypage.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "TBL_DEPARTMENT")
@Getter
@Setter
@SequenceGenerator(
        name = "MYDEPARTMENT_SEQ_NO",
        sequenceName = "DEPARTMENT_SEQ_NO",
        initialValue = 1, allocationSize = 1
)
@ToString
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MYDEPARTMENT_SEQ_NO")
    @Column(name = "DEPT_CODE")
    private int code;

    @Column(name = "NAME")
    private String name;

}
