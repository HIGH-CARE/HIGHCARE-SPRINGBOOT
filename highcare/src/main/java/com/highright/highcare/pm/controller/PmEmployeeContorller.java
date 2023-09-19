package com.highright.highcare.pm.controller;

import com.highright.highcare.common.Criteria;
import com.highright.highcare.common.PageDTO;
import com.highright.highcare.common.PagingResponseDTO;
import com.highright.highcare.common.ResponseDTO;
import com.highright.highcare.pm.dto.*;
import com.highright.highcare.pm.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/pm")
@Slf4j
public class PmEmployeeContorller {

    private final EmployeeService employeeService;

    public PmEmployeeContorller(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /* 사원 전체 조회 */
    @Operation(summary = "사원 전체 조회", description = "모든 사원을 조회합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> selectEmployeeAllList(
            @RequestParam(name = "offset", defaultValue = "1") String offset){
        log.info("start========================================================");
        log.info("offset=============================== : {}", offset);

        int total = employeeService.selectEmployeeTotal();

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(employeeService.selectEmployeeAllList(cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공",  pagingResponseDTO));
    }

    /* 사원 검색 */

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> selectEmployeeList(
            @RequestParam(name = "offset",defaultValue = "1", required = false) String offset, @RequestBody String empName){
        log.info("offset===================> {}", offset);
        log.info("empName==============> {}", empName);

        int total = employeeService.selectEmployeeTotal(empName); // 조건을 추가
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);
        cri.setSearchValue(empName);

        List<PmEmployeeDTO> employeeList = employeeService.selectEmployeeSearchList(cri, empName);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(employeeList);
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공",pagingResponseDTO));

    }

    /* 사원 상세 조회 */
    @Operation(summary = "사원 상세 조회", description = "사원을 상세 조회합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("/member/detail/{empNo}")
    public ResponseEntity<ResponseDTO> selectEmpDetail(@PathVariable int empNo){

        log.info("empName==============> {}", empNo);

        PmEmployeeDTO selectEmpDetail = employeeService.selectEmpDetail(empNo);


        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공",selectEmpDetail));

    }


    /* 사원 등록 */
    @Operation(summary = "사원 등록", description = "사원을 등록합니다", tags = {"PmEmployeeContorller"})
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/member/all")
    public ResponseEntity<ResponseDTO> insertPmEmployee(@ModelAttribute PmEmployeeDTO pmEmployeeDTO){
        log.info("inserPmEmployee=========================> {}", pmEmployeeDTO);


        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK.value(),"사원 등록 성공",
                        employeeService.insertPmEmployee(pmEmployeeDTO)));

    }

    /* 트리뷰 */
    @Operation(summary = "트리뷰", description = "조직도를 조회합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("selectDept")
    public ResponseEntity<ResponseDTO> selectDept() {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),"부서조회성공", employeeService.selectDept()));
    }

    /* 사원 수정 */
    @PutMapping(value = "search")
    public ResponseEntity<ResponseDTO> updateEmployee(@ModelAttribute PmEmployeeDTO pmEmployeeDTO){

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "사원 수정 성공", employeeService.updateEmployee(pmEmployeeDTO)));
    }

    /* 라인 트리뷰 */
    @Operation(summary = "라인 트리뷰", description = "라인조직도를 조회합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("secondDept")
    public ResponseEntity<ResponseDTO> secondDept() {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),"사원조회성공", employeeService.secondDept()));
    }

    /* 출/퇴근 조회 */
    @Operation(summary = "출 퇴근 조회", description = "출퇴근을 조회합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("management/{empNo}")
    public ResponseEntity<ResponseDTO> manageMent(@RequestParam(name = "offset", defaultValue = "1") String offset,
                                                  @PathVariable String empNo )
                                                {

        log.info("start============================================");
        log.info("offset=============================== : {}", offset);
        log.info("empNo=============================== : {}", empNo);

        int selectedEmpNo = employeeService.selectEmpNo(empNo);
        log.info("selectedEmpNo=============================== : {}", selectedEmpNo);
        int total = employeeService.selectEmployeeTotal();

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(employeeService.manageMent(cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));



        Map<String, Object> map = new HashMap<>();
        // 로그인 아이디를
        map.put("manage", employeeService.manageMent(cri));
        map.put("user", employeeService.userInfo(selectedEmpNo));
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공", map));

    }

    /* 출근 */
    @Operation(summary = "출근", description = "출근을 등록합니다", tags = {"PmEmployeeContorller"})
    @PostMapping("management/insert")
    public ResponseEntity<ResponseDTO> insertmanageMent(@RequestBody ManagementDTO managementDTO){
        log.info("insertmanageMent=========================>", managementDTO);

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate currentDate = currentDateTime.toLocalDate();
        LocalTime currentTime = currentDateTime.toLocalTime();

        String yearMonthDay = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        managementDTO.setStartTime(formattedTime);
        managementDTO.setEndTime(null); // 퇴근 시간은 초기값으로 설정/
        managementDTO.setManDate(yearMonthDay); // 년월일만 설정
        managementDTO.setStatus("출근");


        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK.value(),"출근 등록",
                        employeeService.insertmanageMent(managementDTO)));
    }


    /* 퇴근 */
    @Operation(summary = "퇴근", description = "퇴근을 등록합니다.", tags = {"PmEmployeeContorller"})
    @PostMapping("management/update")
    public ResponseEntity<ResponseDTO> updateManageMent(@RequestBody ManagementDTO managementDTO) {
        log.info("updateManageMent=========================>", managementDTO);

        // 출근 여부 확인
        String updateSuccess = employeeService.hasAttendanceRecord(managementDTO);
        System.out.println("updateSuccess ==========================================>>> " + updateSuccess);

        // 업데이트 수행
        if (updateSuccess.equals("success")) {
            return ResponseEntity.ok()
                    .body(new ResponseDTO(HttpStatus.OK.value(), "퇴근 등록 및 업데이트 성공",updateSuccess));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "퇴근 시간 등록 및 업데이트 실패"));
        }

    }

    /* 연차 */
//    @GetMapping("/annual")
//    public ResponseEntity<ResponseDTO> selectAnnual(@ModelAttribute AnnualDTO annualDTO,
//                                                    @RequestParam(name = "offset", defaultValue = "1") String offset){
//        log.info("start========================================================");
//        log.info("offset=============================== : {}", offset);
//
//        int total = employeeService.selectEmployeeTotal();
//
//        Criteria cri = new Criteria(Integer.valueOf(offset), 10);
//
//        List<AnnualDTO> pmAnnuallist = employeeService.selectedAnnaul(cri);
//
//        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
//        pagingResponseDTO.setData(pmAnnuallist);
//        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));
//
//
//        log.info("pmAnnuallist=============================== : {}", pmAnnuallist);
//
//        return ResponseEntity
//                .ok()
//                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공",  pagingResponseDTO));
//    }
    @Operation(summary = "전체 사원 연차 조회", description = "전체 사원 연차를 조회합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("/annual")
    public ResponseEntity<ResponseDTO> selectAnnual(//@ModelAttribute AnnualDTO annualDTO,
            @RequestParam(name = "offset", defaultValue = "1") String offset){
        log.info("start========================================================");
        log.info("offset=============================== : {}", offset);

        int total = employeeService.selectEmployeeTotal();

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        List<AnnualDTO> pmAnnuallist = employeeService.selectedAnnaul(cri);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(pmAnnuallist);
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));


        log.info("pmAnnuallist=============================== : {}", pmAnnuallist);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공",  pagingResponseDTO));
    }
    /* 개인 연차 조회 */
    @Operation(summary = "개인 연차 조회", description = "개인 사원 연차를 조회합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("/annual/detail/{empNo}")
    public ResponseEntity<ResponseDTO> selectPersonalAnnual (@ModelAttribute AnnualDTO annualDTO,
                                                             @PathVariable int empNo ){

        List<AnnualDTO> pmAnnual = employeeService.selectedPersonalAnnaul(empNo);


        log.info("offset=============================== : {}", pmAnnual);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공",  pmAnnual));
    }

    /* 연차 등록 */
    @Operation(summary = "연차 등록", description = "연차를 등록합니다", tags = {"PmEmployeeContorller"})
    @GetMapping("/employee/startDate")
        public ResponseEntity<ResponseDTO> employeeDate(){

        List<PmEmployeeDTO> pmStartDate = employeeService.selectEmployeeStartDate();


        System.out.println("pmStartDate = " + pmStartDate);
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "조회 성공",  pmStartDate));
    }
//    @PostMapping("/addananual")
//    public ResponseEntity<ResponseDTO> calculateAnnualLeaves(@RequestParam int year, @PathVariable int empNo) {
//        employeeService.Annualadd(year, empNo);
//
//        return ResponseEntity.ok().body(new ResponseDTO(200, "연차 계산이 완료되었습니다.", null));
//    }

//    @PostMapping("annual/addAnnual")
//    public ResponseEntity<ResponseDTO> insertAnnual(@RequestBody AnnualDTO annualDTO, @RequestBody ManagementDTO managementDTO){
//        log.info("insertmanageMent=========================>", annualDTO);
//
////        List<PmEmployeeDTO> employees = annualDTO.getAnEmployee(); // 사원 리스트 가져오기
////
////        for(PmEmployeeDTO employee : employees) {
////            Date startDate = (Date) employee.getStartDate(); // 사원 입사일 가져오기
////            LocalDate currentDate = LocalDate.now(); // 현재 날짜 가져오기
////
////            Period period = Period.between(startDate.toLocalDate(), currentDate);
////            int years = period.getYears(); // 입사년도로부터 경과한 연수
////
////            int annualLeave = (years >= 1) ? 15 : 1; // 1년 이상이면 15개, 그 이하면 1개
////
////            employee.setAnnual(annualLeave); // 연차 설정
////        }
//
//        return ResponseEntity.ok()
//                .body(new ResponseDTO(HttpStatus.OK.value(),"연차 등록",
//                        employeeService.insertAnnual(annualDTO, managementDTO)));
//    }
    /* 연차 사용 */

//    @PostMapping("annual/addAnnual")
//    public ResponseEntity<ResponseDTO> insertAnnual(@ModelAttribute AnnualDTO annualDTO) {
//
//        // 결제 신청 보기
//        String Success = employeeService.addAnual(annualDTO);
//        System.out.println("updateSuccess ==========================================>>> " + updateSuccess);
//        // 업데이트 수행
//        //  boolean updateSuccess = (boolean) employeeService.updateManageMent(managementDTO);
//
//        if (Success.equals("success")) {
//            return ResponseEntity.ok()
//                    .body(new ResponseDTO(HttpStatus.OK.value(), "퇴근 등록 및 업데이트 성공",updateSuccess));
//        } else {
//            return ResponseEntity.badRequest()
//                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "퇴근 시간 등록 및 업데이트 실패"));
//        }
//
//    }

}
