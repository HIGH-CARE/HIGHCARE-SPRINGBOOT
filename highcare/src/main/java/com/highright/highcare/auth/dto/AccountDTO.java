package com.highright.highcare.auth.dto;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class AccountDTO {
    
    private String memberId;
    private int empNo;
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력하세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+{}|:;<>,.?~\\-=]).*$", message = "비밀번호는 영문, 숫자, 특수 문자를 포함해야 합니다.")
    private String password;
    private String isTempPwd;
    private Date pwdExpiredDate;


}
