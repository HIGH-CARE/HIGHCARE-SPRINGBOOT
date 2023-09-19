package com.highright.highcare.admin.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class AccessManagerDTO {

    private String id;
    private String isLock;
    private String isInActive;
    private String isExpired;
    private String isWithDraw;
    private LocalDateTime registDate;
    private int loginFailCount;


    private String browser;

    private String device;

    @Builder
    public AccessManagerDTO(String id, String isLock, String isInActive, String isExpired, String isWithDraw, String browser, String device) {
        this.id = id;
        this.isLock = isLock;
        this.isInActive = isInActive;
        this.isExpired = isExpired;
        this.isWithDraw = isWithDraw;
        this.browser = browser;
        this.device = device;
    }
}
