package com.highright.highcare.admin.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MenuGroupDTO {

    private String groupCode;

    private String groupName;

    private String groupStartUrl;

    private List<MenuDTO> menulist;

    @Builder
    public MenuGroupDTO(String groupCode, String groupName, String groupStartUrl) {
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.groupStartUrl = groupStartUrl;
    }
}
