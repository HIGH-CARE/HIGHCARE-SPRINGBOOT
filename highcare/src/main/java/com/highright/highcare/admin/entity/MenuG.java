package com.highright.highcare.admin.entity;

import lombok.*;
import org.hibernate.sql.Insert;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="TBL_MENU")
@NoArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name = "seqMenuCode",
        sequenceName = "SEQ_MENU_CODE",
        initialValue = 1, allocationSize = 1
)
public class MenuG {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seqMenuCode"
    )
    private int menuCode;
    @Column(name = "GROUP_CODE")
    private String groupCode;
    @Column(name = "ID")
    private String id;
    private Date registDate;


    @Builder
    public MenuG(String groupCode, String id, Date registDate) {
        this.groupCode = groupCode;
        this.id = id;
        this.registDate = registDate;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuCode=" + menuCode +
                ", groupCode='" + groupCode + '\'' +
                ", id='" + id + '\'' +
                ", registDate=" + registDate +
                '}';
    }
}
