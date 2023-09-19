package com.highright.highcare.admin.dto;

import com.highright.highcare.auth.dto.AuthAccountDTO;
import com.highright.highcare.auth.dto.LoginMemberDTO;
import com.highright.highcare.auth.entity.AUTHAuthAccount;
import com.highright.highcare.auth.entity.AUTHEmployee;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ADMAccountDTO {

    private String memberId;

    private int empNo;

//    private String password;

    private String isTempPwd;

    private Date pwdExpiredDate;

    private ADMEmployeeDTO employee;
    private List<AuthAccountDTO> roleList; // 유형 authCode (pre임시, draw탈퇴, user정상)
    private AccessManagerDTO accessManager;  // 계정상태 accountStatus (lock잠금, inactive 비활성화, expire만료, withdraw탈퇴)


}
