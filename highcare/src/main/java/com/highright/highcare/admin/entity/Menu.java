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
public class Menu {

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

    @ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩 설정
    @JoinColumn(name ="GROUP_CODE", insertable = false, updatable = false)
    private MenuGroup menuGroup;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false),
//            @JoinColumn(name = "AUTH_CODE", referencedColumnName = "AUTH_CODE", insertable = false, updatable = false)
//    })
//    private ADMAuthAccount authAccount;

    @Builder
    public Menu(String groupCode, String id, Date registDate) {
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
