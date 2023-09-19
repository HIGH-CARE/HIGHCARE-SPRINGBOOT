package com.highright.highcare.admin.entity;

import com.highright.highcare.admin.dto.AccessManagerDTO;
import com.highright.highcare.auth.dto.AuthAccountDTO;
import com.highright.highcare.auth.entity.AUTHAccount;
import com.highright.highcare.auth.entity.AUTHAuthAccount;
import com.highright.highcare.auth.entity.AUTHEmployee;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="TBL_ACCOUNT")
@Getter
@Setter
public class ADMAccount {

    @Id
    @Column(name="ID")
    private String memberId;

    @Column(name="EMP_NO")
    private int empNo;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="IS_TEMP_PWD")
    private String isTempPwd;

    @Column(name="PWD_EXPIRED_DATE")
    private Date pwdExpiredDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "admaAccount", cascade = CascadeType.REMOVE)
    private List<AUTHAuthAccount> roleList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="EMP_NO", insertable = false, updatable = false)
    private AUTHEmployee employee;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "admaAccount", cascade = CascadeType.REMOVE)
    @JoinColumn(name="ID", insertable = false, updatable = false)
    private AccessManager accessManager;

    @Override
    public String toString() {
        return "ADMAccount{" +
                "memberId='" + memberId + '\'' +
                ", empNo=" + empNo +
                ", password='" + password + '\'' +
                ", isTempPwd='" + isTempPwd + '\'' +
                ", pwdExpiredDate=" + pwdExpiredDate +
                ", roleList=" + roleList +
                ", employee=" + employee +
                ", accessManager=" + accessManager +
                '}';
    }
}