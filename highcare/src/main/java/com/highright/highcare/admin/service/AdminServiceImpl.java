package com.highright.highcare.admin.service;

import com.highright.highcare.admin.dto.*;
import com.highright.highcare.admin.entity.*;
import com.highright.highcare.admin.repository.*;
import com.highright.highcare.auth.dto.AccountDTO;
import com.highright.highcare.auth.entity.AUTHAccount;
import com.highright.highcare.auth.entity.AUTHAuthAccount;
import com.highright.highcare.common.AdminCustomBean;
import com.highright.highcare.common.repository.DeptRespository;
import com.highright.highcare.common.repository.JobRepository;
import com.highright.highcare.mypage.Repository.MyProfileFileRepository;
import com.highright.highcare.mypage.Repository.ProfileRepository;
import com.highright.highcare.mypage.dto.JobDTO;
import com.highright.highcare.mypage.entity.Department;
import com.highright.highcare.mypage.entity.Job;

import com.highright.highcare.mypage.entity.MyProfile;
import com.highright.highcare.mypage.entity.MyProfileFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ModelMapper modelMapper;

    private final ADMEmployeeRepository admEmployeeRepository;
    private final ADMAccountRepository admAccountRepository;
    private final ADMAuthAccountRepository admAuthAccountRepository;
    private final JobRepository jobRepository;
    private final DeptRespository deptRespository;
    private final AccessManagerRepository accessManagerRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProfileRepository profileRepository;
    private final MyProfileFileRepository myProfileFileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessManagerListener accessManagerListener;
    private final EntityManager entityManager;


    private final AdminCustomBean customBean;

    private final JavaMailSender javaMailSender;

    @Override
    public Object selectMember(int empNo) {

        if (admAccountRepository.findByEmpNo(empNo) != null) {
            return "fail 해당 사원은 이미 회원입니다.";
        } else {
            ADMEmployee findMember = admEmployeeRepository.findById(empNo).orElse(null);
            if (findMember == null) {
                return "fail 존재하지 않는 사원번호입니다. 사원번호를 다시한번 확인하세요.";
            } else {
                return RequestMemberDTO.builder()
                        .empNo(findMember.getEmpNo())
                        .Name(findMember.getName())
                        .jobName(findMember.getJob().getJobName())
                        .deptName(findMember.getDept().getDeptName())
                        .phone(findMember.getPhone())
                        .email(findMember.getEmail())
                        .build();
            }
        }
    }

    @Transactional
    @Override
    public Object insertAccount(RequestMemberDTO requestMemberDTO) {

        // 일반계정 등록 -> 계정별권한(임시회원) 까지만 진행됨(이후 절차는 회원등록 승인 후)
        log.info("[AdminServiceImpl] insertMember ===== start ");

        String tempPwd = customBean.randomPassword();

        AccountDTO account = AccountDTO.builder()
                .memberId(customBean.idAccountGenerator(requestMemberDTO))
                .empNo(requestMemberDTO.getEmpNo())
                .password(passwordEncoder.encode(tempPwd))
                .isTempPwd("Y")
                .pwdExpiredDate(Date.valueOf(LocalDate.now().plusMonths(3))) // 3개월후 비밀번호 만료
                .build();

        log.info("[AdminServiceImpl] insertMember == account ==={}", account);
        ADMAuthAccountId authAccountId = ADMAuthAccountId.builder()
                .authCode("ROLE_PRE_USER")
                .id(account.getMemberId())
                .build();
        log.info("[AdminServiceImpl] insertMember == authAccountId ==={}", authAccountId);

        try {
            // 계정등록
            ADMAccount joinInfo = admAccountRepository.save(modelMapper.map(account, ADMAccount.class));
            // 권한 등록(pre_user)
            admAuthAccountRepository.save(ADMAuthAccount.builder().id(authAccountId).build());
            // 접근관리 테이블 등록(isLock)
            accessManagerRepository.save(AccessManager.builder().id(account.getMemberId())
                    .registDate(LocalDateTime.now())
                    .isLock("Y")
                    .build());

            // 회원아이디, 임시비밀번호 메일보내기
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(requestMemberDTO.getEmail());
            message.setSubject("[하이케어] 임시회원 아이디, 비밀번호 발송");
            message.setText("회원 아이디 : " + joinInfo.getMemberId() +
                    "회원 비밀번호 : " + tempPwd);

            javaMailSender.send(message);

            log.info("[AdminServiceImpl] insertMember == message ==={}", message);

            // 프로필 만들어두기

            // 프로필, 프로필파일 db 초기데이터 삽입
            MyProfile profile = profileRepository.save(MyProfile.builder().empNo(joinInfo.getEmpNo()).build());
            myProfileFileRepository.save(MyProfileFile.builder()
                    .code(profile.getCode())
                    .name("basicprofile.jpg")
                    .chName("basicprofile.jpg")
                    .profileImgUrl("file:////profileImages/basicprofile.jpg")
                    .date(new Date(System.currentTimeMillis()))
                    .build());

            return "회원 임시등록 성공";
        } catch (Exception e) {
            return "회원 임시등록 실패";
        }
    }

    @Override
    public Object selectAccountList(int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ADMAccount> authAccountList = admAccountRepository.findAllOrderByPreUserOrder(pageRequest);

        log.info("[AdminServiceImpl] selectAccountList authAccountList ==={}", authAccountList);

        return authAccountList.map(account -> modelMapper.map(account, ADMAccountDTO.class));
    }

    @Transactional
    @Override
    public Object updateAccount(String id, UpdateAccountDTO updateAccountDTO) {

        List<ADMAuthAccount> authAccountList = admAuthAccountRepository.findById_Id(id); // 중요!!!
        log.info("[AdminServiceImpl] updateAccount authAccountList ==={}", authAccountList);

        try {
            if (!authAccountList.isEmpty() && updateAccountDTO.getStatus() != null) {

                AccessManager accessManager = accessManagerRepository.findById(id).orElse(null); // ID에 해당하는 AccessManager 조회
                log.info("[AdminServiceImpl] updateAccount accessManager ==={}", accessManager);

                // authAccountList 기존 권한 삭제 처리
                admAuthAccountRepository.deleteAll(authAccountList);

                if (accessManager != null) {

                    String status = updateAccountDTO.getStatus();
                    log.info("[AdminServiceImpl] updateAccount status ==={}", status);
                    switch (status) {
                        case "user":
                            log.info("[AdminServiceImpl] updateAccount user ======================");

                            // 일반 회원 권한 넣기
                            admAuthAccountRepository.save(ADMAuthAccount.builder().id(ADMAuthAccountId.builder().id(id).authCode("ROLE_USER").build()).build());
                            accessManager.setIsLock("N");
                            accessManager.setIsInActive("N");
                            accessManager.setIsExpired("N");
                            accessManager.setIsWithDraw("N");
                            break;
                        case "isLock":
                            log.info("[AdminServiceImpl] updateAccount isLock ======================");

                            admAuthAccountRepository.save(ADMAuthAccount.builder().id(ADMAuthAccountId.builder().id(id).authCode("ROLE_PRE_USER").build()).build());
                            accessManager.setIsLock("Y");
                            accessManager.setIsInActive("N");
                            accessManager.setIsExpired("N");
                            accessManager.setIsWithDraw("N");
                            break;
                        case "isInActive":
                            log.info("[AdminServiceImpl] updateAccount isInActive ======================");

                            admAuthAccountRepository.save(ADMAuthAccount.builder().id(ADMAuthAccountId.builder().id(id).authCode("ROLE_PRE_USER").build()).build());
                            accessManager.setIsLock("N");
                            accessManager.setIsInActive("Y");
                            accessManager.setIsExpired("N");
                            accessManager.setIsWithDraw("N");
                            break;
                        case "isExpired":
                            log.info("[AdminServiceImpl] updateAccount isExpired ======================");

                            admAuthAccountRepository.save(ADMAuthAccount.builder().id(ADMAuthAccountId.builder().id(id).authCode("ROLE_DRAW_USER").build()).build());
                            accessManager.setIsLock("N");
                            accessManager.setIsInActive("N");
                            accessManager.setIsExpired("Y");
                            accessManager.setIsWithDraw("N");
                            break;
                    }
                }
            }

            ADMEmployee employee = admEmployeeRepository.findByEmpNo(Integer.valueOf(updateAccountDTO.getEmpNo()));
            log.info("[AdminServiceImpl] updateAccount employee ======================{} ", employee);
            log.info("[AdminServiceImpl] updateAccount updateAccountDTO ======================{} ", updateAccountDTO);

            if (!updateAccountDTO.getName().isEmpty()) {
                employee.setName(updateAccountDTO.getName());
                log.info("[AdminServiceImpl] updateAccount NAME CHANGE");

            }
            if (!updateAccountDTO.getJobName().isEmpty()) {
                Job job = jobRepository.findByName(updateAccountDTO.getJobName());
                employee.setJobCode(job.getCode());
                log.info("[AdminServiceImpl] updateAccount JOB CHANGE ==={} ", job);
            }
            if (!updateAccountDTO.getDeptName().isEmpty()) {
                Department dept = deptRespository.findByName(updateAccountDTO.getDeptName());
                employee.setDeptCode(dept.getCode());
                log.info("[AdminServiceImpl] updateAccount DEPT CHANGE==={} ", dept);
            }
            if (!updateAccountDTO.getPhone().isEmpty()) {
                employee.setPhone((updateAccountDTO.getPhone()));
                log.info("[AdminServiceImpl] updateAccount PHONE CHANGE");
            }
            if (!updateAccountDTO.getEmail().isEmpty()) {
                employee.setEmail(updateAccountDTO.getEmail());
                log.info("[AdminServiceImpl] updateAccount EMAIL CHANGE==={}");
            }

            return "회원정보 수정 성공";
        } catch (Exception e) {
            return "회원정보 수정 실패";
        }
    }

    @Transactional
    @Override
    public Object deleteAccount(String id) {

        try {
            log.info("[AdminServiceImpl] deleteAccount deleteAccount deleteAccount==={}");


                admAccountRepository.deleteById(id);


            return "계정 삭제 성공";
        } catch (Exception e) {
            return "계정 삭제 실패";
        }
    }

    @Override
    public Object selectJobList() {
        return jobRepository.findAll().stream().map(job -> modelMapper.map(job, JobDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Object selectDepartmentsList() {

        return deptRespository.findAll().stream().map(dept -> modelMapper.map(dept, Department.class)).collect(Collectors.toList());
    }

    @Override
    public Object selectMenuGroupList() {
        return menuGroupRepository.findAllByOrderByGroupCodeAsc().stream().map(menu -> modelMapper.map(menu, MenuGroupDTO.class));
    }

    @Transactional
    @Override
    public Object insertMenuManagers(MenuManagerDTO menuManagerDTO) {
        log.info("[AdminServiceImpl] insertMenuManagers menuDTOList ======================{} ", menuManagerDTO);

        try {
            for (String id : menuManagerDTO.getId()) {
                Optional<ADMAuthAccount> account = admAuthAccountRepository.findById_IdAndId_AuthCode(id, "ROLE_MANAGER");
                log.info("[AdminServiceImpl] insertMenuManagers account ======================{} ", account);

                if (account.isEmpty()) {
                    log.info("[AdminServiceImpl] insertMenuManagers isEmpty");
                    admAuthAccountRepository.save(ADMAuthAccount.builder().id(ADMAuthAccountId.builder().id(id).authCode("ROLE_MANAGER").build()).build());
                }
            }


            // 2. 매니저별 관리 메뉴 등록
        /* 먼저 해당 id로 db를 조회한다. 요청받은 메뉴코드가 데이터에 이미 존재하면 무시하고,
        없는 메뉴코드이면 insert 진행, db에는 있으나 요청에 없는 메뉴코드가 있다면 delete 진행
        */

            for (String id : menuManagerDTO.getId()) {
                log.info("[AdminServiceImpl] insertMenuManagers id ======================{} ", id);

                String[] requestGroupCodes = menuManagerDTO.getGroupCode();
                // DB에서 저장된 groupCode 목록 가져오기

                // 제네릭 사용하여 원하는 entity로 반환받기
                List<MenuMapping> menus = menuRepository.findAllById(id);
                log.info("[AdminServiceImpl] insertMenuManagers menus ======================{} ", menus);
                List<String> dbGroupCodes = menus.stream().map(menuMapping -> menuMapping.getGroupCode()).collect(Collectors.toList());

                log.info("[AdminServiceImpl] insertMenuManagers dbGroupCodes ======================{} ", dbGroupCodes);

                // 요청에서 온 groupCode 중 DB에 없는 것들을 필터링하여 insert
                List<String> newGroupCodes = Arrays.stream(requestGroupCodes)
                        .filter(groupCode -> !dbGroupCodes.contains(groupCode))
                        .collect(Collectors.toList());

                // DB에 있는 groupCode 중 요청에서 온 목록에 없는 것들을 필터링하여 delete
                List<String> removedGroupCodes = dbGroupCodes.stream()
                        .filter(groupCode -> !Arrays.asList(requestGroupCodes).contains(groupCode))
                        .collect(Collectors.toList());


                log.info("[AdminServiceImpl] insertMenuManagers newGroupCodes ======================{} ", newGroupCodes);
                log.info("[AdminServiceImpl] insertMenuManagers removedGroupCodes ======================{} ", removedGroupCodes);


                // insert 작업
                newGroupCodes.forEach(groupCode -> {
                    Menu menuManager = Menu.builder()
                            .id(id)
                            .groupCode(groupCode)
                            .registDate(new Date(System.currentTimeMillis()))
                            .build();
                    log.info("[AdminServiceImpl] insertMenuManagers menuManager ======================{} ", menuManager);

                    menuRepository.save(menuManager);
                });


                // delete 작업 (기존 매니저만 진행되어야 함)
                if (!dbGroupCodes.isEmpty())
                    removedGroupCodes.forEach(groupCode -> {
                        menuRepository.deleteByGroupCodeAndId(String.valueOf(groupCode), id);
                    });
            }

            return "매니저 메뉴 등록 성공";
        } catch (Exception e) {

            return "매니저 메뉴 등록 실패";
        }
    }

    @Transactional
    @Override
    public Object deleteMenuManagers(String[] ids) {
        try {
            for (String id : ids) {
                Optional<ADMAuthAccount> authAccountOptional = admAuthAccountRepository.findById_IdAndId_AuthCode(id, "ROLE_MANAGER");
                List<Menu> menus = menuRepository.findById(id);
                log.info("[AdminServiceImpl] deleteMenuManagers menus ======================{} ", menus);
//                    // 관련 메뉴 삭제 (자식테이블)
                if (!menus.isEmpty()) {
                    menuRepository.deleteAll(menus);
                }
//
                if (authAccountOptional.isPresent()) {
                    // 그 후 매니저 권한 삭제
                    admAuthAccountRepository.delete(authAccountOptional.get());
                }
            }


            return "매니저 삭제 성공";
        } catch (Exception e) {
            return "매니저 삭제 실패";
        }
    }

//    @Transactional
//    @Override
//    public Object selectAccessLog(int page) {
//
//        Page<ADMAccount> paging = admAccountRepository.findAllByOrderByAccessManager_RegistDateDesc(PageRequest.of(page, 15));
//        log.info("[AdminServiceImpl] selectAccessLog paging ======================{} ", paging);
//
//        return paging;
//
//        return admAccountRepository.findAllByOrderByAccessManager_RegistDateDesc().stream().map(account ->
//                modelMapper.map(account, ADMAccountDTO.class)).collect(Collectors.toList());
//    }

    @Override
    public Page<ADMAccountDTO> selectSearchMemberLog(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ADMAccount> paging = admAccountRepository.findByEmployee_NameContaining(keyword, pageRequest);

        log.info("[AdminServiceImpl] selectSearchMemberLog paging ======================{} ", paging);

        return paging.map(p -> modelMapper.map(p, ADMAccountDTO.class));
    }

    @Override
    public Page<ADMAccountDTO> selectSearchMemberDateLog(LocalDateTime start, LocalDateTime end, int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ADMAccount> paging = admAccountRepository.findByAccessManager_RegistDateBetweenOrderByAccessManager_RegistDateDesc(start, end, pageRequest);

        log.info("[AdminServiceImpl] selectSearchMemberDateLog paging ======================{} ", paging);

        //페이지에이블로 받기 . 페이징 인자로 넘기고/ 사이즈, 페이지 받고 -- 전체크기를 알고싶으면 페이지로 받아.

        return paging.map(p -> modelMapper.map(p, ADMAccountDTO.class));
    }

    @Transactional
    @Override
    public Object insertAllUsers(String[] ids) {

        try {
         // authAccountList 기존 권한 삭제 처리
//            admAuthAccountRepository.deleteAuthAccountBulk(ids);
            // 일반 회원 권한 넣기
//            admAuthAccountRepository.insertAuthAccountBulk(ids);
            // 계정상태 변경
//            admAuthAccountRepository.updateAccountBulk(ids, 'N');
//            accessManager.setIsLock("N");
//            accessManager.setIsInActive("N");
//            accessManager.setIsExpired("N");
//            accessManager.setIsWithDraw("N");

            for(String id : ids){

                AccessManager accessManager = entityManager.find(AccessManager.class, id);
                log.info("[AdminServiceImpl] insertAllUsers accessManager ======================{} ", accessManager);

                accessManager.setIsLock("N");
                accessManager.setIsInActive("N");
                accessManager.setIsExpired("N");
                accessManager.setIsWithDraw("N");
                entityManager.merge(accessManager);

//                ADMAuthAccount admAuthAccount = (ADMAuthAccount) admAuthAccountRepository.findById_Id(id);
//                log.info("[AdminServiceImpl] insertAllUsers admAuthAccount ======================{} ", admAuthAccount);
            }


            return "회원정보 수정 성공";
        } catch (Exception e) {
            return "회원정보 수정 실패";
        }
    }

    @Override
    public Page<ADMAccountDTO> getAccountsByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ADMAccount> paging = admAccountRepository.findAllByOrderByAccessManager_RegistDateDesc(pageRequest);

        log.info("[AdminServiceImpl] selectSearchMemberDateLog paging ======================{} ", paging);

        return paging.map(p -> modelMapper.map(p, ADMAccountDTO.class));

    }


}
