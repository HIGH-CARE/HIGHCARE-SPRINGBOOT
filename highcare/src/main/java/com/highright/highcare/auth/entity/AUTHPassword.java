package com.highright.highcare.auth.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="TBL_CHANGE_PASSWORD")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(
        name = "seqAuthPassword",
        sequenceName = "SEQ_AUTH_PASSWORD",
        initialValue = 1, allocationSize = 1
)
public class AUTHPassword {

    @Id
    @Column(name="CHANGE_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seqAuthPassword"
    )
    private int changeNo;

    @Column(name="PREV_PASSWORD")
    private String prevPassword;

    @Column(name="CHANGE_DATE")
    private Timestamp changeDate;

    @Column(name="ID")
    private String id;


    @Builder
    public AUTHPassword(String prevPassword, String id, Timestamp changeDate) {
        this.prevPassword = prevPassword;
        this.id = id;
        this.changeDate = changeDate;
    }
}
