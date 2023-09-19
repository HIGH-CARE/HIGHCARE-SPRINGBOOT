package com.highright.highcare.admin.service;

import com.highright.highcare.admin.dto.*;
import com.highright.highcare.admin.entity.ADMAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    Object selectMember(int empNo);

    Object insertAccount(RequestMemberDTO requestMemberDTO);

    Object selectAccountList(int page, int size);


    Object deleteAccount(String id);

    Object updateAccount(String id, UpdateAccountDTO updateAccountDTO);

    Object selectJobList();

    Object selectDepartmentsList();

    Object selectMenuGroupList();

    Object insertMenuManagers(MenuManagerDTO menuManagerDTO);

    Object deleteMenuManagers(String[] ids);

//    Object selectAccessLog();

    //    Object selectAccessLog(int page);
//    Object selectAccessLog(int page);

    Page<ADMAccountDTO> getAccountsByPage(int page, int size);

    //    Object selectSearchMemberLog(String keyword);
    Page<ADMAccountDTO> selectSearchMemberLog(String keyword, int page, int size);

    //    Object selectSearchMemberDateLog(LocalDateTime start, LocalDateTime end);
    Page<ADMAccountDTO> selectSearchMemberDateLog(LocalDateTime start, LocalDateTime end, int page, int size);


    Object insertAllUsers(String[] ids);

//    Page<ADMAccountDTO> selectAllAccountForLog(PageRequest of);
}
