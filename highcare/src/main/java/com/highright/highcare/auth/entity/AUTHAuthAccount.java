package com.highright.highcare.auth.entity;


import com.highright.highcare.admin.entity.ADMAccount;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="TBL_AUTH_ACCOUNT")
@Getter
@Setter
@EqualsAndHashCode
public class AUTHAuthAccount implements Serializable {

    private static final long serialVersionUID = 1L; // 추가

    @Id
    @Column(name="AUTH_CODE")
    private String authCode;

    @Id
    @Column(name="ID")
    private String id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID")
    private ADMAccount admaAccount;

    @Override
    public String toString() {
        return "AUTHAuthAccount{" +
                "authCode='" + authCode + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}