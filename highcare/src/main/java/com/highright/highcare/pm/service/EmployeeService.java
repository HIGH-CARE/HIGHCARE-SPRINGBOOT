package com.highright.highcare.pm.service;

import com.highright.highcare.auth.entity.AUTHAccount;
import com.highright.highcare.auth.repository.AccountRepository;
import com.highright.highcare.common.Criteria;

import com.highright.highcare.pm.dto.*;
import com.highright.highcare.pm.entity.*;
import com.highright.highcare.pm.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final PmDepartmentRepository pmDepartmentRepository;

    private final ReEmployeeRepository reEmployeeRepository;

    private final ManagementEmRepository managementEmRepository;

    private final AccountRepository accountRepository;

    private final CareerRepository careerRepository;

    private final CertificationRepository certificationRepository;

    private final MilitaryRepository militaryRepository;

    private final AnAnualRepository anAnualRepository;

    private final AnEmployeeRepository anEmployeeRepository;

    private final AnnualEmployeeRepository annualEmployeeRepository;


    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper,
                           PmDepartmentRepository pmDepartmentRepository, ReEmployeeRepository reEmployeeRepository
                            , ManagementEmRepository managementEmRepository
                            , AccountRepository accountRepository
                            , CareerRepository careerRepository
                            , CertificationRepository certificationRepository
                            , MilitaryRepository militaryRepository
                            , AnAnualRepository anAnualRepository
                            , AnEmployeeRepository anEmployeeRepository
                            , AnnualEmployeeRepository annualEmployeeRepository){
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.pmDepartmentRepository = pmDepartmentRepository;
        this.reEmployeeRepository = reEmployeeRepository;
        this.managementEmRepository = managementEmRepository;
        this.accountRepository = accountRepository;
        this.careerRepository = careerRepository;
        this.certificationRepository = certificationRepository;
        this.militaryRepository = militaryRepository;
        this.anAnualRepository = anAnualRepository;
        this.anEmployeeRepository = anEmployeeRepository;
        this.annualEmployeeRepository = annualEmployeeRepository;

    }

    /* 토탈 */
    public int selectEmployeeTotal() {
        List<PmEmployee> pmEmployeeList = employeeRepository.findByIsResignation('N');
        log.info("size: {}", pmEmployeeList);
        log.info("size: {}", pmEmployeeList.size());

        return pmEmployeeList.size();
    }

    /* 사원 전체 조회 */
    public List<PmEmployeeDTO> selectEmployeeAllList(Criteria cri){
        System.out.println("cri ==========================> " + cri);
        int index = cri.getPageNum() -1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("isResignation").descending());
        System.out.println("paging ==========================> " + paging);

//        Page<PmEmployee> result = employeeRepository.findByIsResignation('N', paging);
        Page<PmEmployee> result = employeeRepository.findByIsResignation('N', paging);
        System.out.println("result ==========================> " + result);

        List<PmEmployeeDTO> employeeallList = result.stream()
                .map(pmEmployee -> modelMapper
                        .map(pmEmployee, PmEmployeeDTO.class)).collect(Collectors.toList());

        return employeeallList;
    }

    /* 사원 상세 조회 */
    public PmEmployeeDTO selectEmpDetail(int empNo){

        PmEmployee empDetail = employeeRepository.findByEmpNo(empNo);
        System.out.println("result ==========================> " + empDetail);
        System.out.println("result ==========================> " + empNo);


        PmEmployeeDTO pmEmployee = modelMapper.map(empDetail, PmEmployeeDTO.class);
        System.out.println("pmEmployee ==========================> " + pmEmployee);


        return pmEmployee;
    }
    // reEmployee


    /* 사원 검색 */
    public List<PmEmployeeDTO> selectEmployeeList(String empName) {
        List<PmEmployee> pmemployeeList = employeeRepository.findByEmpNo(Integer.valueOf(empName));
        System.out.println("pmemployeeList ==============> " + pmemployeeList);
        List<PmEmployeeDTO> employeeList = pmemployeeList.stream()
                .map(pmEmployee -> modelMapper.map(pmEmployee, PmEmployeeDTO.class))
                .collect(Collectors.toList());

        return employeeList;
    }

    /* 사원 이름 조회 */
    public int selectEmployeeTotal(String empName) {
        List<PmEmployee> pmEmployeeList = employeeRepository.findByEmpName(empName);
        log.info("size: {}", pmEmployeeList);
        log.info("size: {}", pmEmployeeList.size());

        return pmEmployeeList.size();
    }

    public int selectEmpNo(String empNo){

        Optional<AUTHAccount> authAccount = accountRepository.findById(empNo);
        System.out.println(authAccount.get().getEmployee().getEmpNo());
        int result = authAccount.get().getEmployee().getEmpNo();
        return result;
    }

//    /* 사원 번호 조회 */
//    public int selectEmpDatail(int empNo) {
//       PmEmployee pmEmployee = employeeRepository.findByEmpNo(empNo);
//        log.info("size: {}", pmEmployee);
//        log.info("size: {}", pmEmployee.size());
//
//        return pmEmployee.size();
//    }

    /* 사원 검색 */
    public List<PmEmployeeDTO> selectEmployeeSearchList(Criteria cri, String search) {
        System.out.println("selectEmployeeSearchList  cri ==========================> " + cri);
        int index = cri.getPageNum() -1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("empName").descending());
        System.out.println("selectEmployeeSearchList paging ==========================> " + paging);

        Page<PmEmployee> result = employeeRepository.findByEmpName(search, paging);
        System.out.println("selectEmployeeSearchList result ==========================> " + result);

        List<PmEmployeeDTO> employeedetailList = result.stream()
                .map(mgEmployee -> modelMapper
                        .map(mgEmployee, PmEmployeeDTO.class)).collect(Collectors.toList());

        return employeedetailList;
    }


    /* 사원 등록 */
    @Transactional
    public String insertPmEmployee(@ModelAttribute PmEmployeeDTO pmEmployeeDTO) {
        log.info("insertPmEmployee start==================");
        log.info("insertPmEmployee pmEmployeeDTO ================== " + pmEmployeeDTO );

        try {
            // PmEmployee 엔터티 생성 및 저장
            PmEmployee insertPmEmployee = modelMapper.map(pmEmployeeDTO, PmEmployee.class);
            insertPmEmployee.setIsResignation('N');
            employeeRepository.save(insertPmEmployee);

            // Career 테이블에 데이터 삽입
            if (pmEmployeeDTO.getCareer() != null) {
                for (CareerDTO careerDTO : pmEmployeeDTO.getCareer()) {
                    Career career = modelMapper.map(careerDTO, Career.class);
                    // PmEmployee와 연관 설정
                    career.setEmployees(insertPmEmployee);

                    careerRepository.save(career);
                }
            }

            // Certification 테이블에 데이터 삽입
            if (pmEmployeeDTO.getCertification() != null) {
                for (CertificationDTO certificationDTO : pmEmployeeDTO.getCertification()) {
                    Certification certification = modelMapper.map(certificationDTO, Certification.class);
                    // PmEmployee와 연관 설정
                    certification.setEmployees(insertPmEmployee);
                    certificationRepository.save(certification);
                }
            }

            // Military 테이블에 데이터 삽입
            if (pmEmployeeDTO.getMilitary() != null) {
                for (MilitaryDTO militaryDTO : pmEmployeeDTO.getMilitary()) {
                    Military military = modelMapper.map(militaryDTO, Military.class);

                    Date startDate = militaryDTO.getStartDate();

                    if(startDate == null) {
                        military.setIsWhether('N');
                    } else {
                        military.setIsWhether('Y');
                    }

                    // PmEmployee와 연관 설정
                    military.setEmployees(insertPmEmployee);
                    militaryRepository.save(military);
                }
            }
            log.info("insertpmEmployee ============================end");
            return "사원 등록 성공";
        } catch (Exception e) {
            log.error("사원 등록 실패", e);
            return "사원 등록 실패";
        }
    }



    /* 사원 및 부서 조회 // 트리뷰 */
    public PmDepartmentResult selectDept() {
        List<PmDepartment> deptList = pmDepartmentRepository.findAll();
        PmDepartmentResult result =  deptList.stream().map(PmDepartmentResult::new).collect(Collectors.toList()).get(0);
        return result;
    }



    /* 사원 수정 */
    @Transactional
    public String updateEmployee(PmEmployeeDTO pmEmployeeDTO) {
        log.info("[ProductService] updateEmployee Start ===================================");
        log.info("[ProductService] pmEmployeeDTO : " + pmEmployeeDTO);

        int result = 0;
        try {
            PmEmployee pmEmployee = employeeRepository.findById(pmEmployeeDTO.getEmpNo()).get();

            pmEmployee.setEmpName(pmEmployeeDTO.getEmpName());
            pmEmployee.setEmpEmail(pmEmployeeDTO.getEmpEmail());
            pmEmployee.setPhone(String.valueOf(pmEmployeeDTO.getIsResignation()));
            pmEmployee.setDeptCode(pmEmployeeDTO.getDeptCode());
//            pmEmployee.setJob(pmEmployeeDTO.getJobCode());
            pmEmployee.setAddress(pmEmployeeDTO.getAddress());
            pmEmployee.setEducation(pmEmployeeDTO.getEducation());
            pmEmployeeDTO.setTelephone(pmEmployeeDTO.getTelephone());

            result = 1;

        } catch (Exception e) {
            log.info("updateEmployee exception!!=====================");
            throw new RuntimeException(e);
        }
        log.info("updateEmployee end========================");

        return (result > 0)? "사원 수정 성공": "사원 수정 실패";

    }

    /* 라인 트리뷰 */
    public Object secondDept() {

        log.info("[EmployeeService] secondDept start ==================================================");

        List<PmDepartment>  pmList = pmDepartmentRepository.findAll();

        List<DeAndEmpDTO> list = new ArrayList<>();
        for(PmDepartment pm : pmList) {
            if (pm.getDeptCode() != 0) {
                DeAndEmpDTO dept = new DeAndEmpDTO();
                dept.setId(pm.getDeptCode());
                dept.setText(pm.getName());
                dept.setParent(pm.getUpperCode() == null ? 0 : pm.getUpperCode());
                dept.setDroppable(true);
                dept.setJobName(null);
                dept.setDeptName(null);

                list.add(dept);
            }
        }
        List<ReEmployee> empList = reEmployeeRepository.findAll();
        System.out.println("empList = " + empList);
        for(ReEmployee emp : empList) {
            if (emp.getEmpNo() != 0) {
                DeAndEmpDTO dept = new DeAndEmpDTO();
                dept.setId(emp.getEmpNo());
                dept.setText(emp.getEmpName() + " " + emp.getReJob().getJobName());
//                dept.setParent(emp.getDeptCode());
                dept.setParent(emp.getReDepartment().getDeptCode());
                dept.setName(emp.getEmpName());
                dept.setJobName(emp.getReJob().getJobName());
                dept.setDeptName(emp.getReDepartment().getName());




                list.add(dept);
            }
        }
        System.out.println("result===================>" + list);
        return list;
    }

    /* 출 퇴근 조회 */
    public List<ManagementResult> manageMent(Criteria cri) {
        System.out.println("cri ============================> " + cri);
        int index = cri.getPageNum() -1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("manNo").descending());
        System.out.println("paging ==========================> " + paging);

        Page<Management> result = managementEmRepository.findAll(paging);

        System.out.println("result ==========================> " + result);

        List<Management> manageList = result.getContent(); // 조회된 결과 리스트

        System.out.println("manageList ==========================> " + manageList);
        List<ManagementResult> managementResult = manageList.stream()
                .sorted(Comparator.comparing(Management::getManNo).reversed()) // manNo 내림차순 정렬
                .map(ManagementResult::new)
                .collect(Collectors.toList());


        return managementResult;
    }

    public ManagementDTO userInfo(Integer empNo) {


        Management result = managementEmRepository.findByEmpNo(empNo);

        System.out.println("result ==========================> " + result);

        ManagementDTO management = null;
        if(result != null){

            management = modelMapper.map(result, ManagementDTO.class);
            management.setStartTime(result.getStartTime());
            System.out.println("result ==========111================> " + management);
            management.setEndTime(result.getEndTime());
            System.out.println("result ===========111===============> " + management);
        }
       // Management manageList = result.get(); // 조회된 결과 리스트

        return management;
    }



    /*출근시간 등록*/
    @Transactional
    public Object insertmanageMent(@ModelAttribute ManagementDTO managementDTO) {
        log.info("insertmanageMent start==================");
        log.info("insertmanageMent ManagementDTO ================== " + managementDTO );


        Optional<Management> existingRecords = managementEmRepository.findByManDateAndEmpNo(managementDTO.getManDate(), managementDTO.getEmpNo());

        if (!existingRecords.isEmpty()) {
            log.info("Record with the same manDate and empNo already exists");
            return "exit";
        }

        int result = 0;

        try {
            Management insertmanageMent =  modelMapper.map(managementDTO, Management.class);
            managementEmRepository.save(insertmanageMent);
            result = 1;
        } catch (Exception e) {
            log.error("Error while inserting management record", e);
            throw new RuntimeException(e);
        }
        log.info("RESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULT"+ result);
        log.info("insertmanageMent ============================end");
        return (result > 0) ? "stSuceess" : "fail";
    }


    /* 퇴근 */
    @Transactional
    public String updateManageMent(Management management) {
        log.info("updatemanageMent start==================");
        log.info("updatemanageMent Management ================== " + management );

        int result = 0;

        try {
           // Management updatemanageMent =  modelMapper.map(managementDTO, Management.class);
            managementEmRepository.save(management);
            result = 1;
        }catch (Exception e) {
            System.out.println("check");
            throw new RuntimeException(e);
        }
        log.info("updatemanageMent ============================end");
        return (result > 0)? "success": "fail";


    }

    /* 퇴근 */
    public String hasAttendanceRecord(ManagementDTO managementDTO) {

        System.out.println("empNo =============================> " + managementDTO);

        // 현재 날짜를 구합니다.
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        System.out.println("formattedDate =============================> " + formattedDate);

        // 해당 날짜의 출근 기록을 조회합니다.
        Optional<Management> attendanceRecords = managementEmRepository.findByManDateAndEmpNo(formattedDate, managementDTO.getEmpNo());

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();
        LocalDate currentDate2 = currentDateTime.toLocalDate();

        String yearMonthDay = currentDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 퇴근 정보 업데이트
        //managementDTO.setManDate(yearMonthDay);
        attendanceRecords.get().setEndTime(formattedTime);
        attendanceRecords.get().setStatus("퇴근");

        String result = updateManageMent(attendanceRecords.get());
        // 출근 기록이 존재하면 true를 반환, 없으면 false를 반환합니다.

        System.out.println("result =12837129873681236712873618273621873681276832======== " + result);
        return result;
    }

    /* 연차 조회 */
    // 1. 사원, 부서, 직급, 입사일 등 employee, department, job등 필요한 정보가 추가적으로 존재
    // 2. 저위에 테이블과 annual테이블을 엮어서 전체로 들고와야함
    // 3.
    public List<AnnualDTO> selectedAnnaul(Criteria cri){
        System.out.println("cri ==========================> " + cri);
        int index = cri.getPageNum() -1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count);
        System.out.println("paging ==========================> " + paging);


        Page<AnAnual> result = anAnualRepository.findAll(paging);
        System.out.println("result ==========================> " + result);

        List<AnAnual> annEmployee = result.getContent();

        System.out.println("result ==========================> " + annEmployee);



      List<AnnualDTO> annEmployeeList = result.getContent().stream()
                .map(ananul -> modelMapper
                        .map(ananul, AnnualDTO.class)).collect(Collectors.toList());
//        List<AnnualDTO> annEmployeeList = null;


        return annEmployeeList;
    }

    /* 개인 연차 조회 */
    public List<AnnualDTO> selectedPersonalAnnaul(int empNo) {

        List<AnAnual> anAnual = anAnualRepository.findByEmpNo(empNo);

        List<AnnualDTO> annual = anAnual.stream().map(anl -> modelMapper.map(anl,  AnnualDTO.class)).collect(Collectors.toList());

        return annual;
    }


    /* 연차 등록 */

//    public void Annualadd(int year) {
//        LocalDate startDateOfTheYear = LocalDate.of(year, 1, 1);
//        LocalDate endDateOfTheYear = LocalDate.of(year, 12, 31);
//
//        List<PmEmployee> employees = employeeRepository.findAll();
//
//        for (PmEmployee employee : employees) {
//            LocalDate hireDate = employee.getStartDate().toLocalDate();
//
//            if (hireDate != null) {
//                Period period = Period.between(hireDate, endDateOfTheYear);
//
//                int years = period.getYears();
//
//                if (years >= 1) {
//                    AnAnual anAnual = new AnAnual();
//                    anAnual.setBasicAnnual(15); // 1년 이상 근무한 경우 연차를 15로 설정
//                    anAnual.setUseAnnual(0); // 초기 사용 연차를 0으로 설정
//                    anAnual.setAddAnnual(0); // 초기 추가 연차를 0으로 설정
//                    anAnual.setTotalAnnual(15); // 총 연차를 15로 설정
//                    anAnual.setReason("연차 계산");
//                    // ann_no와 apv_no를 설정해야 한다면 설정
//
//                    anAnualRepository.save(anAnual);
//                } else {
//                    AnAnual annual = new AnAnual();
//
//                    annual.setBasicAnnual(1); // 1년 미만 근무한 경우 연차를 1로 설정
//                    annual.setUseAnnual(0); // 초기 사용 연차를 0으로 설정
//                    annual.setAddAnnual(0); // 초기 추가 연차를 0으로 설정
//                    annual.setTotalAnnual(1); // 총 연차를 1로 설정
//                    annual.setReason("연차 계산");
//                    // ann_no와 apv_no를 설정해야 한다면 설정
//
//                    anAnualRepository.save(annual);
//                }
//            }
//        }
//    }

    ////////////////////
//    @Transactional
//    public List<PmEmployeeDTO> selectEmployeeStartDate() {
//
//        List<AnEmployee> pmstartDate = anEmployeeRepository.findAll();
//        System.out.println("pmstartDate = " + pmstartDate);
//        List<PmEmployeeDTO> startDate = pmstartDate.stream().map(std -> modelMapper.map(std,  PmEmployeeDTO.class)).collect(Collectors.toList());
//
//        for(PmEmployeeDTO pme : startDate) {
//
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//
//            int year = Integer.valueOf(sdf.format(pme.getStartDate()));
//            System.out.println("year = " + year);
//            int currentYear = Integer.valueOf(sdf.format(new Date()));
//
//            System.out.println("(currentYear - year) = " + (currentYear - year));
//
//            int yearsOfWork = currentYear - year; // 현재 년도와 입사 년도의 차이로 연차 계산
//
//            AnAnual annual = new AnAnual();
//
//            if (yearsOfWork >= 1) {
//                annual.setBasicAnnual(15); // 1년 이상 근무한 경우 연차를 15로 설정
//            } else {
//                annual.setBasicAnnual(1); // 1년 미만 근무한 경우 연차를 1로 설정
//            }
//
//            annual.setEmpNo(pme.getEmpNo());
//            System.out.println("annual = " + annual);
//            annual.setUseAnnual(0); // 초기 사용 연차를 0으로 설정
//            annual.setAddAnnual(0); // 초기 추가 연차를 0으로 설정
//            annual.setAddAnnual(0);
//            annual.setApvNo("705");
////            annual.setTotalAnnual(0);
//            annual.setTotalAnnual(annual.getBasicAnnual()); // 총 연차를 기본 연차로 설정
//
////            pme.setAnnual(annual);
//
//            pme.setAnAnual(annual);
//
//            System.out.println("annual.getEmpNo() = " + annual.getEmpNo());
//
////            pme.setAnnual(annual);
//            AnEmployee findEmp = anEmployeeRepository.findByEmpNo(pme.getEmpNo());
//
//
//            System.out.println("findEmp = " + findEmp);
//            List<AnAnual> anList = findEmp.getAnAnual();
//            System.out.println("annual = " + annual.getAddAnnual());
//            System.out.println("anList = " + anList.size());
////            System.out.println("anList = " + anList.get(0).getAnEmployee());
//            anList.add(annual);
//            findEmp.setAnAnual(anList);
//            System.out.println("findEmp =================== " + findEmp.getAnAnual().get(0).getReason());
//            anEmployeeRepository.save(findEmp);
//
//
//
//        }
//
//        return startDate;
//    }
//}
    @Transactional
    public List<PmEmployeeDTO> selectEmployeeStartDate() {
        List<AnnualEmployee> pmstartDate = annualEmployeeRepository.findAll();
        List<PmEmployeeDTO> startDate = pmstartDate.stream().map(std -> modelMapper.map(std, PmEmployeeDTO.class)).collect(Collectors.toList());

        for (PmEmployeeDTO pme : startDate) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

            int year = Integer.valueOf(sdf.format(pme.getStartDate()));
            int currentYear = Integer.valueOf(sdf.format(new Date()));

            int yearsOfWork = currentYear - year;

            AnAnual annual = new AnAnual();

            if (yearsOfWork >= 1) {
                annual.setBasicAnnual(15);
            } else {
                annual.setBasicAnnual(1);
            }

//            annual.setEmpNo(pme.getEmpNo());
            annual.setEmpNo(Integer.parseInt(String.valueOf(pme.getEmpNo())));
            System.out.println("annual = " + annual);
            System.out.println("pme = " + pme.getEmpNo());
            annual.setUseAnnual(0);
            annual.setAddAnnual(0);
            annual.setApvNo("863");
//            annual.setApvNo(String.valueOf(785));
            annual.setTotalAnnual(annual.getBasicAnnual());

            pme.setAnAnual(annual);

            AnnualEmployee findEmp = annualEmployeeRepository.findByEmpNo(pme.getEmpNo());
            List<AnAnual> anList = findEmp.getAnAnual();
            anList.add(annual);
            findEmp.setAnAnual(anList);

            // 여기서 anAnual의 repository에 값을 저장
            anAnualRepository.save(annual);
//            anEmployeeRepository.save(findEmp);
        }

        return startDate;
    }
    /////////////////
//    public Object insertAnnual(AnnualDTO annualDTO, ManagementDTO managementDTO) {
//        log.info("insertAnnual start==================");
//        log.info("insertAnnual annualDTO ================== " + annualDTO );
//
//        Optional<AnAnual> annualadd = anAnualRepository.findByEmpNo();
//
//        if (!annualadd.isEmpty()) {
//            return "exit";
//        }
//
//        int result = 0;
//
//        try {
//            AnAnual insertAnnual =  modelMapper.map(annualDTO, AnAnual.class);
//            anAnualRepository.save(insertAnnual);
//            result = 1;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        log.info("RESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULTRESULT"+ result);
//        log.info("insertmanageMent ============================end");
//        return (result > 0) ? "anSuceess" : "fail";
//    }

//    public String addAnual(AnnualDTO annualDTO) {
//
//        List<AnAnual> addAnuallist = anAnualRepository.findByEmpNo(annualDTO.getEmpNo());
//
//        addAnuallist.get().setUseAnnual();
//
//        attendanceRecords.get().setEndTime(formattedTime);
//        attendanceRecords.get().setStatus("퇴근");
//
//        String result = updateManageMent(attendanceRecords.get());
//        // 출근 기록이 존재하면 true를 반환, 없으면 false를 반환합니다.
//
//        System.out.println("result =12837129873681236712873618273621873681276832======== " + result);
//    }


    // 연도별 연차를 가져올거면 연차를 가져와서 원투매니로 해당내용연결 employee
    // 전체연차중에 사원이한명씩붙음 // 전체연차조회하면 전체사원이나옴
    // 연차기준으로 매핑되어있는 사원을들고오기
    //!!!!!!!!!!!!!!!!!!! 연차엔터티안에 사원을  묶을것  사원에는 부서가 붙어있으니까 다딸려서 들어올거임

}

//employee랑 annual 랑 묶어서 입사년도




