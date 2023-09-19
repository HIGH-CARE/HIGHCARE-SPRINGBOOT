package com.highright.highcare.admin.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.highright.highcare.admin.dto.*;
import com.highright.highcare.admin.entity.ADMAccount;
import com.highright.highcare.admin.service.AdminService;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.common.PageDTO;
import com.highright.highcare.common.PagingResponseDTO;
import com.highright.highcare.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Slf4j
@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "사원 조회 요청", description = "회원 가입을 위한 사원조회 인증이 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/member")
    public ResponseEntity<ResponseDTO> selectMember(@RequestParam String empNo) {
        log.info("================= empNo ===== {}", empNo);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "사원 조회", adminService.selectMember(Integer.valueOf(empNo))));
    }

    // 인서트 회원신청
    @Operation(summary = "회원 등록 요청", description = "회원 등록이 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/memberjoin")
    public ResponseEntity<ResponseDTO> insertAccount(@RequestBody RequestMemberDTO requestMemberDTO) {
        log.info("[AdminController] insertAccount requestMemberDTO===={}", requestMemberDTO);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "회원 등록 신청", adminService.insertAccount(requestMemberDTO)));
    }

    @Operation(summary = "일반회원 일괄 요청", description = "일반회원으로의 전환을 일괄로 요청합니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/allusers")
    public ResponseEntity<ResponseDTO> insertAllUsers(@RequestBody String[] ids) {
        log.info("[AdminController] insertAllUsers ids===={}", ids);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "일반회원 신청(일괄)", adminService.insertAllUsers(ids)));
    }

    @Operation(summary = "전체 회원 조회 요청", description = "전체 회원 조회 요청이 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/memberlist")
    public ResponseEntity<ResponseDTO> selectAccountList( @RequestParam(defaultValue = "0") int page
                                                        , @RequestParam(defaultValue = "15") int size) {

        Page<ADMAccountDTO> accountDTOPage = (Page<ADMAccountDTO>) adminService.selectAccountList(page, size);

        log.info("[AdminServiceImpl] selectAccountList accountDTOPage ==={}", accountDTOPage);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "전체 회원 조회", accountDTOPage));
    }

    @Operation(summary = "회원 계정상태 수정 요청", description = "회원 계정상태(정상, 임시, 차단, 만료, 탈퇴예정) 업데이트가 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/member/{id}")
    public ResponseEntity<ResponseDTO> updateAccount(@PathVariable String id, @RequestBody UpdateAccountDTO updateAccountDTO) {

        log.info("[AdminController] updateAccount id ===={}", id);
        log.info("[AdminController] updateAccount updateAccountDTO===={}", updateAccountDTO);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "회원 계정상태 수정 완료", adminService.updateAccount(id, updateAccountDTO)));
    }

    @Operation(summary = "회원 삭제(계정 삭제) 요청", description = "회원 탈퇴(계정 삭제)가 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/member/{id}")
    public ResponseEntity<ResponseDTO> updateAccount(@PathVariable String id) {
        log.info("[AdminController] updateAccount id===={}", id);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "회원 삭제(탈퇴)", adminService.deleteAccount(id)));
    }


    @Operation(summary = "회원 직급 조회 요청", description = "회원의 직급 조회가 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/jobs")
    public ResponseEntity<ResponseDTO> selectJobList() {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "직급 조회", adminService.selectJobList()));
    }

    @Operation(summary = "회원 부서 조회 요청", description = "회원의 부서 조회가 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @GetMapping("/department")
    public ResponseEntity<ResponseDTO> selectDepartmentList() {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "부서 조회", adminService.selectDepartmentsList()));
    }

    @Operation(summary = "매니저 권한 메뉴 조회 요청", description = "매니저 권한에서 접속 가능한 메뉴들을 조회합니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/menugroup")
    public ResponseEntity<ResponseDTO> selectMenuGroupList() {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "메뉴그룹 조회", adminService.selectMenuGroupList()));
    }


    @Operation(summary = "매니저 권한 메뉴 등록 요청", description = "매니저 권한 및 메뉴 등록이 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/managers")
    public ResponseEntity<ResponseDTO> insertMenuManagers(@RequestBody MenuManagerDTO menuManagerDTO) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "매니저 메뉴등록", adminService.insertMenuManagers(menuManagerDTO)));
    }

    @Operation(summary = "매니저 권한 삭제 요청", description = "매니저 권한이 삭제됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/managers")
    public ResponseEntity<ResponseDTO> deleteMenuManagers(@RequestParam("ids") String[] ids) {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "매니저 삭제", adminService.deleteMenuManagers(ids)));
    }

    @Operation(summary = "회원 접속이력 조회 요청", description = "회원들의 접속 이력(성공/실패) 이력이 조회됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/access")
    public ResponseEntity<ResponseDTO> selectAccessLog(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "15") int size) {
        Page<ADMAccountDTO> accountPageDTO = adminService.getAccountsByPage(page, size);


        System.out.println("accountPage =====================>>>= " + accountPageDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "접속로그 조회", accountPageDTO));
    }

    @Operation(summary = "회원 접속이력 이름 검색 요청", description = "회원들의 접속 이력(성공/실패)에서 이름 검색이 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/access/search")
    public ResponseEntity<ResponseDTO> selectSearchMemberLog(@RequestParam String keyword,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "15") int size) {

        Page<ADMAccountDTO> searchPageDTO = adminService.selectSearchMemberLog(keyword, page, size);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "회원 접속로그 검색", searchPageDTO));
    }

    @Operation(summary = "회원 접속이력 날짜 검색 요청", description = "회원들의 접속 이력(성공/실패)에서 날짜 검색이 진행됩니다.", tags = {"AdminController"})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/access/date")
    public ResponseEntity<ResponseDTO> selectAccessLog(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
            , @RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "15") int size
    ) {

        LocalDateTime startDate = LocalDateTime.of(start, LocalTime.of(0, 0, 0));
        LocalDateTime endDate = LocalDateTime.of(end, LocalTime.of(23, 59, 59));

        //페이지에이블로 받기 . 페이징 인자로 넘기고/ 사이즈, 페이지 받고 -- 전체크기를 알고싶으면 페이지로 받아.
//        Page<ADMAccountDTO> accountDTOPage = adminService.selectAllAccountForLog(PageRequest.of(page, size));
        Page<ADMAccountDTO> accountDTOPage = adminService.selectSearchMemberDateLog(startDate, endDate, page, size);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "사원 접속로그 날짜 검색", accountDTOPage));
    }


}
