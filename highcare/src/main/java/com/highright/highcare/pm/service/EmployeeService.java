package com.highright.highcare.pm.service;

import com.highright.highcare.common.Criteria;

//import com.highright.highcare.pm.dto.PmEmployeeAndDepartmentDTO;
import com.highright.highcare.pm.dto.PmEmployeeDTO;
import com.highright.highcare.pm.entity.PmDepartment;
import com.highright.highcare.pm.entity.PmDepartmentResult;
import com.highright.highcare.pm.entity.PmEmployee;
//import com.highright.highcare.pm.entity.PmEmployeeAndPmDepartment;
import com.highright.highcare.pm.entity.PmEmployeeResult;
import com.highright.highcare.pm.repository.EmployeeRepository;
import com.highright.highcare.pm.repository.PmDepartmentRepository;
//import com.highright.highcare.pm.repository.PmJobRepository;
//import com.highright.highcare.pm.repository.PmEmployeeAndPmDepartmentRepository;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final PmDepartmentRepository pmDepartmentRepository;

//    private final PmEmployeeAndPmDepartmentRepository pmEmployeeAndPmDepartmentRepository;

//    private final PmJobRepository pmJobRepository;


    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper, PmDepartmentRepository pmDepartmentRepository) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.pmDepartmentRepository = pmDepartmentRepository;
//        this.pmJobRepository = pmJobRepository;
//        this.pmEmployeeAndPmDepartmentRepository = pmEmployeeAndPmDepartmentRepository;
    }

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
    public List<PmEmployeeDTO> selectEmployeeList(String empName) {
//        log.info("empNo==================================> {}",empNo);
//        List<PmEmployee> pmemployeeList = employeeRepository.findByEmpNo(Integer.valueOf(empNo));
//        List<PmEmployee> pmemployeeList = employeeRepository.findByEmpNo(Integer.valueOf(1));
        List<PmEmployee> pmemployeeList = employeeRepository.findByEmpNo(Integer.valueOf(empName));
        System.out.println("pmemployeeList ==============> " + pmemployeeList);
        List<PmEmployeeDTO> employeeList = pmemployeeList.stream()
                .map(pmEmployee -> modelMapper.map(pmEmployee, PmEmployeeDTO.class))
                .collect(Collectors.toList());

        return employeeList;
    }

    public int selectEmployeeTotal(String empName) {
        List<PmEmployee> pmEmployeeList = employeeRepository.findByEmpName(empName);
        log.info("size: {}", pmEmployeeList);
        log.info("size: {}", pmEmployeeList.size());

        return pmEmployeeList.size();
    }

    public List<PmEmployeeDTO> selectEmployeeSearchList(Criteria cri, String search) {
        System.out.println("selectEmployeeSearchList  cri ==========================> " + cri);
        int index = cri.getPageNum() -1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("empName").descending());
        System.out.println("selectEmployeeSearchList paging ==========================> " + paging);

        Page<PmEmployee> result = employeeRepository.findByEmpName(search, paging);
        System.out.println("selectEmployeeSearchList result ==========================> " + result);

        List<PmEmployeeDTO> employeedetailList = result.stream()
                .map(pmEmployee -> modelMapper
                        .map(pmEmployee, PmEmployeeDTO.class)).collect(Collectors.toList());

        return employeedetailList;
    }

    /* 사원 등록 */
    @Transactional
    public String insertPmEmployee(@ModelAttribute PmEmployeeDTO pmEmployeeDTO) {
        log.info("insertPmEmployee start==================");
        log.info("insertPmEmployee pmEmployeeDTO ================== " + pmEmployeeDTO );

        int result = 0;

        try {
            PmEmployee insertPmEmployee = modelMapper.map(pmEmployeeDTO, PmEmployee.class);
            employeeRepository.save(insertPmEmployee);

            result = 1;
        }catch (Exception e){
            System.out.println("check");
            throw new RuntimeException(e);
        }

        log.info("insertpmEmployee ============================end");
        return (result > 0)? "사원 등록 성공": "사원 등록 실패";
    }

    /* 사원 및 부서 조회*/
    public PmDepartmentResult selectDept() {
        List<PmDepartment> deptList = pmDepartmentRepository.findAll();
        PmDepartmentResult result =  deptList.stream().map(PmDepartmentResult::new).collect(Collectors.toList()).get(0);
        return result;

    }
//    @Transactional(rollbackFor = Exception.class)
//    public List<PmDepartmentResult> getPmDepartmentList() {
//
////        List<PmDepartmentResult> results = pmDepartmentRepository.findAll().stream()
////                .filter(pm -> pm.getUpperCode() == null)
////                .map(PmDepartmentResult::of)
////                .sorted((dept1, dept2) -> {
////                    if (dept1.getJobcode()!= null && dept2.getJobcode() == null) {
////                        return -1; // dept1을 앞으로
////                    } else if (dept1.getJobcode() == null && dept2.getJobcode() != null) {
////                        return 1;  // dept2를 앞으로
////                    }
////                    return 0; // 그 외의 경우는 순서 변경 없음
////                })
////                .collect(Collectors.toList());
//
//        List<PmDepartmentResult> results = pmDepartmentRepository.findAll().stream().filter(pm -> pm.getUpperCode() == null)
//                .map(PmDepartmentResult::insertChild)
//                .map(PmDepartmentResult::of).collect(Collectors.toList());
//        System.out.println("results = " + results);
//        return results;                                          //findAll다찾아서-> stream리스트로 뽑아줌 그중에서 결과값이 true 즉,upperCode가 null인값만 받겠다.
//
//
////        return results;
//    }

//    private List<Deemployee> chemployee;
//
//    public List<Deemployee> getChemployee() {
//        return chemployee;
//    }
//
//    public void setChemployee(List<Deemployee> chemployee) {
//        this.chemployee = chemployee;
//    }

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

//    /* 사원 부서 조회 */
//    public Object selectEmployeeWithDepartment(Criteria cri) {
//        log.info("PmEmployeeAndPmDepartment ============================start");
//
//        int index = cri.getPageNum() - 1;
//        int count = cri.getAmount();
//        Pageable paging = PageRequest.of(index, count, Sort.by("isResignation").descending());
//
//        Page<PmEmployeeAndPmDepartment> result = pmEmployeeAndPmDepartmentRepository.findAll(paging);
//        List<PmEmployeeAndPmDepartment> productList = (List<PmEmployeeAndPmDepartment>)result.getContent();
//
//
//        System.out.println("pmEmployeeAndPmDepartmentRepository paging ==========================> " + paging);
//        System.out.println("pmEmployeeAndPmDepartmentRepository result ==========================> " + result);
//
//        List<PmEmployeeAndDepartmentDTO> empdeList = result.stream()
//                .map(pmEmployee -> modelMapper
//                        .map(pmEmployee, PmEmployeeAndDepartmentDTO.class)).collect(Collectors.toList());
//
//        return empdeList;
//
//
//    }

//    @Transactional(rollbackFor = Exception.class)
//    public List<PmJobResult> getPmResultList(){
//
//        List<PmJobResult> results = pmJobRepository.findAll().stream().map(PmJobResult::of).collect(Collectors.toList());
//        return results;
//    }
//    @Transactional(rollbackFor = Exception.class)
//    public List<>



//    public List<PmEmployeeDTO> selectEmployeeList(String empNo) {
//        List<PmEmployee> pmemployeeList = employeeRepository.findByEmployee(empNo);
//
//        List<PmEmployeeDTO> employeeList = pmemployeeList.stream()
//                .map(pmEmployee -> {
//                    PmEmployeeDTO pmEmployeeDTO = modelMapper.map(pmEmployee, PmEmployeeDTO.class);
//                    return pmEmployeeDTO;
//                })
//                .collect(Collectors.toList());
//
//        return employeeList;
//    }
}





//    public List<PmEmployeeDTO> selectEmployeeList(String empNo) {
//
////        PmEmployee pmEmployee = employeeRepository.findByemployee(empNo).get();
////        EmployeeDTO employeeDTO = modelMapper.map(pmEmployee, EmployeeDTO.class);
//
////        List<EmployeeDTO> result = employeeRepository.findByemployee(empNo).get();
////        List<EmployeeDTO> employeeList = result.stream()
//
//        List<PmEmployee> pmemployeeList = employeeRepository.findByemployee(empNo);
//        List<PmEmployeeDTO> employeeList = pmemployeeList.stream()
//                .map((pmEmployee -> modelMapper
//                        .map(pmEmployee, PmEmployeeDTO.class)).collect(Collectors.toList());
//
//        return employeeList;
//
//    }

//    @Transactional
//    public Long resist(PmEmployee pmemployee) {
//        employeeRepository.save(pmemployee);
//        return Long.valueOf(pmemployee.getEmpNo());
//
//    }
//
//    public List<PmEmployee> findEmployee(){
//        return employeeRepository.findAll();
//    }
//
//    public PmEmployee findEmployeeOne(Long empNo){
//        return employeeRepository.findOne(empNo);
//    }

// 1. 전체데이터조회 2.페이징 필요하면 리스트로뿌려주니까 페이징추가하고 3.검색어추가하고