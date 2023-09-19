package com.highright.highcare.admin.entity;

import com.highright.highcare.admin.dto.MenuDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="TBL_MENU_GROUP")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MenuGroup {

    @Id
    @Column
    private String groupCode;

    @Column
    private String groupName;

    @Column
    private String groupStartUrl;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="GROUP_CODE")
    private List<MenuG> menulist;


    @Builder
    public MenuGroup(String groupCode, String groupName, String groupStartUrl) {
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.groupStartUrl = groupStartUrl;
    }


}
