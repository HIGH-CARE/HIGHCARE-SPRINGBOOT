package com.highright.highcare.reservation.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResourceDTO {
    private int resourceCode;
    private String resourceName;
    private String area;
    private String location;
    private String serviceGuide;
    private java.sql.Date creationDate;
    private java.sql.Date modifiedDate;
    private char deleteYn;
    private String startTime;
    private String endTime;
    private ResourceCategoryDTO resourceCategoryDTO;
    private int categoryCode;

    private String fileUrl;

}
