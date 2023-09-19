package com.highright.highcare.mypage.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TBL_MY_PROFILE")
@Getter
@Setter
@SequenceGenerator(
        name = "MYPROFILE_SEQ_NO",
        sequenceName = "PROFILE_SEQ_NO",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
public class MyProfile {

    @Id
    @GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "MYPROFILE_SEQ_NO")
    @Column(name = "PROFILE_CODE")
    private int code;

    @Column(name="EMP_NO")
    private int empNo;

    @OneToOne
    @JoinColumn(name="EMP_NO", insertable = false, updatable = false)
    private MyEmployee myEmployee;

    @OneToOne
    @JoinColumn(name = "PROFILE_CODE", referencedColumnName = "PROFILE_CODE")
    private MyProfileFile myProfileFile;

    @Override
    public String toString() {
        return "MyProfile{" +
                "code=" + code +
                ", empNo=" + empNo +

                ", myEmployee=" + myEmployee +
                ", myProfileFile=" + myProfileFile +
                '}';
    }

    @Builder
    public MyProfile(int empNo) {
        this.empNo = empNo;
    }
}
