package com.highright.highcare.admin.dto;


import com.highright.highcare.admin.entity.MenuGroup;
import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MenuDTO {

    private int menuCode;
    private String groupCode;
    private String id;
    private Date registDate;

    private MenuGroupDTO menuGroup;


    @Builder
    public MenuDTO(int menuCode,  String groupCode, String id, Date registDate) {
        this.menuCode = menuCode;

        this.groupCode = groupCode;
        this.id = id;
        this.registDate = registDate;
    }
}
