package com.highright.highcare.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TBL_MY_BSCD")
@Getter
@Setter
@ToString
public class MyBscd {

    @Id
    @Column(name = "BSCD_CODE")
    private int code;

    @Column(name = "BSCD_NAME")
    private String name;

    @Column(name = "PHONE_NUMBER")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "COMPANY")
    private String company;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "EMP_NO")
    private MyEmployee myEmp;


}
