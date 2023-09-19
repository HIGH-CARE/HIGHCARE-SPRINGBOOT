package com.highright.highcare.mypage.dto;

//import com.highright.highcare.mypage.entity.AnnEmployee;
import lombok.*;

import javax.persistence.Column;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MyAnnualDTO {


    private int empNo;
    private int bAn;
    private int useAn;
    private int addAn;

    @Column(name = "TOTAL_ANNUAL")
    private int totalAn;

    private int annNo;
    private String apvNo;


    private MyApvVationDTO myApvVation;





}
