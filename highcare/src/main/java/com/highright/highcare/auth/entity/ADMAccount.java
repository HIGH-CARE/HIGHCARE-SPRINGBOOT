package com.highright.highcare.auth.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="TBL_ACCOUNT")
@Getter
@ToString
public class ADMAccount {

    @Id
    @Column(name="ID")
    private String memberId;

    @OneToOne
    @JoinColumn(name="EMP_NO")
    private ADMEmployee employee;

    @Column(name="PASSWORD")
    private String password;

    @OneToMany
    @JoinColumn(name="ID")
    private List<ADMAuthAccount> roleList;

}
