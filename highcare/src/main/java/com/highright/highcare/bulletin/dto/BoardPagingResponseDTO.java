package com.highright.highcare.bulletin.dto;

import com.highright.highcare.common.PageDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardPagingResponseDTO {

    private Object data;
    private Object detail;
    private PageDTO pageInfo;
    private int total;

}
