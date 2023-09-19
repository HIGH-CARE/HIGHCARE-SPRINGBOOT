package com.highright.highcare.reservation.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResourceFileDTO {
    private int fileCode;
    private String type;
    private String originalFileName;
    private String changedFileName;
    private java.sql.Date creationDate;
    private char deleteYn;
    private java.sql.Date modifiedDate;
    private int resourceCode;
}
