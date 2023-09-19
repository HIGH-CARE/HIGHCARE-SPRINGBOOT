package com.highright.highcare.mypage.dto;

import com.highright.highcare.mypage.entity.MyProfileFile;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MyProfileDTO {

    private int code;
    private int empNo;

    private MyEmployeeDTO myEmployee;
    private MyProfileFileDTO myProfileFile;

}

