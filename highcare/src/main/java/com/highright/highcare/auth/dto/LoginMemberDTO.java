package com.highright.highcare.auth.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginMemberDTO implements UserDetails {

    private int empNo;
    private String id;
    private String Name;
    private String password;

    private String isTempPwd;
    private Date pwdExpiredDate;

    private String deptName;
    private String jobName;

    private String loginType;

    private String authNumber;

    private String browser;

    private String device;

    private List<AuthAccountDTO> roleList;

    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return getId();
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


    @Builder
    public LoginMemberDTO(int empNo, String id, String name, String password, String isTempPwd, Date pwdExpiredDate, String deptName, String jobName, String loginType, String authNumber, String browser, String device, List<AuthAccountDTO> roleList) {
        this.empNo = empNo;
        this.id = id;
        Name = name;
        this.password = password;
        this.isTempPwd = isTempPwd;
        this.pwdExpiredDate = pwdExpiredDate;
        this.deptName = deptName;
        this.jobName = jobName;
        this.loginType = loginType;
        this.authNumber = authNumber;
        this.browser = browser;
        this.device = device;
        this.roleList = roleList;
    }

}
