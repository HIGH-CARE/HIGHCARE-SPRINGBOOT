package com.highright.highcare.admin.entity;

import com.highright.highcare.admin.service.AccessManagerListener;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="TBL_ACCESS_MANAGER")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AccessManagerListener.class)
public class AccessManager {

    @Id
    @Column(name="ID")
    private String id;

    @Column(name="REGIST_DATE")
    private LocalDateTime registDate;

    @Column(name="LOGIN_TOTAL_COUNT")
    private int loginTotalCount;

    @Column(name="LOGIN_FAIL_COUNT")
    private int loginFailCount;

    @Column(name = "IS_LOCK")
    @ColumnDefault("'Y'") // 기본값 'Y'로 설정
    private String isLock;

    @Column(name = "IS_INACTIVE")
    @ColumnDefault("'N'") // 기본값 'N'로 설정
    private String isInActive;

    @Column(name = "IS_EXPIRED")
    @ColumnDefault("'N'") // 기본값 'N'로 설정
    private String isExpired;

    @Column(name = "IS_WITHDRAW")
    @ColumnDefault("'N'") // 기본값 'N'로 설정
    private String isWithDraw;

    @Column(name="EXPIRED_DATE")
    private Date expiredDate;


    @Column(name="WITHDRAW_DATE")
    private Date withDrawDate;

    private String browser;

    private String device;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID")
    private ADMAccount admaAccount;

    @Builder
    public AccessManager(String id, LocalDateTime registDate, int loginTotalCount, int loginFailCount, String isLock, String isInActive, String isExpired, String isWithDraw, Date expiredDate, Date withDrawDate, String browser, String device) {
        this.id = id;
        this.registDate = registDate;
        this.loginTotalCount = loginTotalCount;
        this.loginFailCount = loginFailCount;
        this.isLock = isLock;
        this.isInActive = isInActive;
        this.isExpired = isExpired;
        this.isWithDraw = isWithDraw;
        this.expiredDate = expiredDate;
        this.withDrawDate = withDrawDate;

        this.browser = browser;
        this.device = device;
    }
    @Override
    public String toString() {
        return "AccessManager{" +
                "id='" + id + '\'' +
                ", registDate=" + registDate +
                ", loginTotalCount=" + loginTotalCount +
                ", loginFailCount=" + loginFailCount +
                ", isLock='" + isLock + '\'' +
                ", isInActive='" + isInActive + '\'' +
                ", isExpired='" + isExpired + '\'' +
                ", isWithDraw='" + isWithDraw + '\'' +
                ", expiredDate=" + expiredDate +
                ", withDrawDate=" + withDrawDate +
                ", browser='" + browser + '\'' +
                ", device='" + device + '\'' +
                '}';
    }
}
