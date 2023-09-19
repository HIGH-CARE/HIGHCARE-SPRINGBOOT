package com.highright.highcare.admin.dto;


import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MenuManagerDTO {


    private String[] groupCode;
    private String[] id;

    @Builder
    public MenuManagerDTO(String[] groupCode, String[] id) {

        this.groupCode = groupCode;
        this.id = id;
    }
}
