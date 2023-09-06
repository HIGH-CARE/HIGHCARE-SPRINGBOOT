package com.highright.highcare.approval.service;

import com.highright.highcare.approval.dto.*;
import com.highright.highcare.approval.entity.*;
import com.highright.highcare.approval.repository.*;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.pm.entity.PmEmployee;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ApprovalBizService {

    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;
    private final ApvFormMainRepository apvFormMainRepository;
    private final ApvFormRepository apvFormRepository;
    private final ApvLineRepository apvLineRepository;
    private final ApvMeetingLogRepository apvMeetingLogRepository;
    private final ApvBusinessTripRepository apvBusinessTripRepository;



    @Autowired
    public ApprovalBizService(ModelMapper modelMapper,
                              ApprovalService approvalService,
                              ApvFormMainRepository apvFormMainRepository,
                              ApvFormRepository apvFormRepository,
                              ApvLineRepository apvLineRepository,
                              ApvMeetingLogRepository apvMeetingLogRepository,
                              ApvBusinessTripRepository apvBusinessTripRepository
    ) {
        this.modelMapper = modelMapper;
        this.approvalService = approvalService;
        this.apvFormMainRepository = apvFormMainRepository;
        this.apvFormRepository = apvFormRepository;
        this.apvLineRepository = apvLineRepository;
        this.apvMeetingLogRepository = apvMeetingLogRepository;
        this.apvBusinessTripRepository = apvBusinessTripRepository;
    }


    /* 전자결재 - 업무: biz1 기안서 */
    @Transactional
    public Boolean insertApvFormWithLines(ApvFormDTO apvFormDTO, List<ApvLineDTO> apvLineDTOs, List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Biz1-insertApvForm --------------- 기안서 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            // ApvForm 생성 및 저장
            ApvForm apvForm = modelMapper.map(apvFormDTO, ApvForm.class);
            ApvForm savedApvForm = apvFormRepository.save(apvForm);

            // ApvLineDTO를 ApvLine 엔티티로 매핑하고 ApvNo 설정
            apvLineDTOs.forEach(apvLine -> apvLine.setApvNo(savedApvForm.getApvNo()));
            List<ApvLine> apvLineList = apvLineDTOs.stream().map(item -> modelMapper.map(item, ApvLine.class)).collect(Collectors.toList());

            // ApvLines 설정
            savedApvForm.setApvLines(apvLineList);
            System.out.println("apvLineList = " + apvLineList);

            // 첨부파일 등록
            if (apvFileDTO != null && !apvFileDTO.isEmpty()) {
                List<ApvFile> apvFiles = approvalService.insertFiles(savedApvForm.getApvNo(), apvFileDTO);
                savedApvForm.setApvFiles(apvFiles);
                System.out.println("apvFiles = " + apvFiles);
            }


            // 승인 상태 확인 후 결재 상태 변경
            int approved = apvLineRepository.apvNoAllApproved(savedApvForm.getApvNo());
            if (approved == 0) {
                apvFormRepository.updateApvStatusToCompleted(savedApvForm.getApvNo());
            }

            log.info("[ApprovalService] Biz1-insertApvForm --------------- 기안서 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error - Biz1-insertApvForm : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무: biz1 기안서 수정 */
    @Transactional
    public Boolean putApvFormWithLines(ApvFormWithLinesDTO apvFormWithLinesDTO) {
        log.info("[ApprovalService] Biz1-putApvFormWithLines --------------- start ");
        log.info("[ApprovalService] apvFormWithLinesDTO {}", apvFormWithLinesDTO);

        try {
            ApvFormDTO apvFormDTO = apvFormWithLinesDTO.getApvFormDTO();
            System.out.println("=========== 1. apvFormDTO ===========");
            System.out.println(apvFormDTO);

            List<ApvLineDTO> apvLineDTO = apvFormDTO.getApvLines();
            System.out.println("=========== 2. apvLineDTO ===========");
            System.out.println(apvLineDTO);

            ApvForm apvForm = modelMapper.map(apvFormDTO, ApvForm.class);
            System.out.println("=========== 3. apvForm ===========");
            System.out.println(apvForm);

            List<ApvLine> apvLineList = apvForm.getApvLines();
            System.out.println("apvLineList = " + apvLineList);

            ApvForm savedApvForm = apvFormRepository.save(apvForm);
            System.out.println("=========== 4. savedApvForm ===========");
            System.out.println(savedApvForm);



            log.info("[ApprovalService] Biz1-putApvFormWithLines --------------- end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Biz1-putApvFormWithLines : " + e.getMessage());
            return false;
        }
    }
//

//    @Transactional
//    public Boolean putApvFormWithLines(ApvFormWithLinesDTO apvFormWithLinesDTO) {
//        log.info("[ApprovalService] biz1-putApvFormWithLines --------------- 시작 ");
//        log.info("[ApprovalService] apvFormWithLinesDTO {}", apvFormWithLinesDTO);
//
//        try {
//            // ApvFormWithLinesDTO에서 필요한 데이터 추출
//            ApvFormDTO apvFormDTO = apvFormWithLinesDTO.getApvFormDTO();
//            List<ApvLineDTO> apvLineDTOList = apvFormDTO.getApvLines();
//
//            // ApvForm 생성 및 저장
//            ApvForm apvForm = modelMapper.map(apvFormDTO, ApvForm.class);
//            ApvForm savedApvForm = apvFormRepository.save(apvForm);
//
//            log.info("[ApprovalService] biz1-putApvFormWithLines --------------- 종료 ");
//            return true;
//        } catch (Exception e) {
//            log.error("[ApprovalService] 오류 발생 - biz1-putApvFormWithLines : " + e.getMessage());
//            return false;
//        }
//    }



    /* 전자결재 - 업무: biz2 회의록 */
    @Transactional
    public Boolean insertApvMeetingLog(ApvFormDTO apvFormDTO, List<ApvLineDTO> apvLineDTOs, List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalHrmService] Biz2-insertApvMeetingLog --------------- 회의록 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            List<ApvMeetingLogDTO> apvMeetingLogDTO = apvFormDTO.getApvMeetingLogs();

            // ApvFormMain을 저장하고 생성된 ApvNo를 가져오기
            ApvFormMain apvFormMain = modelMapper.map(apvFormDTO, ApvFormMain.class);
            apvFormMain = apvFormMainRepository.save(apvFormMain);
            Long apvNo = apvFormMain.getApvNo();

            // ApvMeetingLogDTO를 ApvMeetingLog 엔티티로 매핑하고 ApvNo를 설정
            List<ApvMeetingLog> apvMeetingLogList = apvMeetingLogDTO.stream()
                    .map(dto -> {
                        ApvMeetingLog apvMeetingLog = modelMapper.map(dto, ApvMeetingLog.class);
                        apvMeetingLog.setApvNo(apvNo);
//                        apvMeetingLog.getApvForm().setApvNo(apvNo);
                        return apvMeetingLog;
                    })
                    .collect(Collectors.toList());

            // ApvMeetingLog 엔티티를 저장
            apvMeetingLogList = apvMeetingLogRepository.saveAll(apvMeetingLogList);

            // ApvLineDTO를 ApvLine 엔티티로 매핑하고 ApvNo를 설정
            List<ApvLine> apvLineList = apvLineDTOs.stream()
                    .map(dto -> {
                        ApvLine apvLine = modelMapper.map(dto, ApvLine.class);
                        apvLine.setApvNo(apvNo);
                        return apvLine;
                    })
                    .collect(Collectors.toList());

            // 첨부파일 등록을 위해 서비스로 DTO전달
            List<ApvFile> apvFiles = new ArrayList<>();
            if (apvFileDTO != null && !apvFileDTO.isEmpty()) {
                apvFiles = approvalService.insertFiles(apvFormMain.getApvNo(), apvFileDTO);
            }
            apvFormMain.setApvFiles(apvFiles);
            System.out.println("apvFiles = " + apvFiles);

            // ApvLine, ApvFile 엔티티를 ApvFormMain에 설정
            apvFormMain.setApvLines(apvLineList);
            apvFormMain.setApvFiles(apvFiles);
            System.out.println("apvFormMain = " + apvFormMain);

            // 승인 상태를 확인하고 업데이트
            if (apvLineRepository.apvNoAllApproved(apvNo) == 0) {
                apvFormRepository.updateApvStatusToCompleted(apvNo);
            }
            log.info("[ApprovalService] Biz2 insertApvMeetingLog --------------- 회의록 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Biz2 insertApvMeetingLog : " + e.getMessage());
            return false;
        }
    }


    /* 전자결재 - 업무 : biz3 출장신청서 */
    @Transactional
    public Boolean insertApvBusinessTrip(ApvFormDTO apvFormDTO, List<ApvLineDTO> apvLineDTOs, List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalHrmService] Biz3-insertApvBusinessTrip --------------- 출장신청서 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            List<ApvBusinessTripDTO> apvBusinessTripDTO = apvFormDTO.getApvBusinessTrips();

            // ApvFormMain을 저장하고 생성된 ApvNo를 가져오기
            ApvFormMain apvFormMain = modelMapper.map(apvFormDTO, ApvFormMain.class);
            apvFormMain = apvFormMainRepository.save(apvFormMain);
            Long apvNo = apvFormMain.getApvNo();

            // ApvBusinessTripDTO를 ApvBusinessTrip 엔티티로 매핑하고 ApvNo를 설정
            List<ApvBusinessTrip> apvBusinessTripList = apvBusinessTripDTO.stream()
                    .map(dto -> {
                        ApvBusinessTrip apvBusinessTrip = modelMapper.map(dto, ApvBusinessTrip.class);
                        apvBusinessTrip.setApvNo(apvNo);
//                        apvBusinessTrip.getApvForm().setApvNo(apvNo);
                        return apvBusinessTrip;
                    })
                    .collect(Collectors.toList());

            // ApvBusinessTrip 엔티티를 저장
            apvBusinessTripList = apvBusinessTripRepository.saveAll(apvBusinessTripList);

            // ApvLineDTO를 ApvLine 엔티티로 매핑하고 ApvNo를 설정
            List<ApvLine> apvLineList = apvLineDTOs.stream()
                    .map(dto -> {
                        ApvLine apvLine = modelMapper.map(dto, ApvLine.class);
                        apvLine.setApvNo(apvNo);
                        return apvLine;
                    })
                    .collect(Collectors.toList());

            // 첨부파일 등록을 위해 서비스로 DTO전달
            List<ApvFile> apvFiles = new ArrayList<>();
            if (apvFileDTO != null && !apvFileDTO.isEmpty()) {
                apvFiles = approvalService.insertFiles(apvFormMain.getApvNo(), apvFileDTO);
            }
            apvFormMain.setApvFiles(apvFiles);
            System.out.println("apvFiles = " + apvFiles);

            // ApvLine, ApvFile 엔티티를 ApvFormMain에 설정
            apvFormMain.setApvLines(apvLineList);
            apvFormMain.setApvFiles(apvFiles);
            System.out.println("apvFormMain = " + apvFormMain);

            // 승인 상태를 확인하고 업데이트
            if (apvLineRepository.apvNoAllApproved(apvNo) == 0) {
                apvFormRepository.updateApvStatusToCompleted(apvNo);
            }

            log.info("[ApprovalService] Biz3 insertApvBusinessTrip --------------- 출장신청서 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Biz3 insertApvBusinessTrip : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무 : biz3 출장신청서 조회 */
    public List<ApvBusinessTripDTO> selectApvBusinessTrip(int empNo) {
        log.info("[ApprovalService] Biz3-searchApvFormWithLines --------------- 출장신청서 조회 start ");

        List<ApvBusinessTrip> apvBusinessTripList = apvBusinessTripRepository.findByEmpNo(empNo);

        if (apvBusinessTripList == null || apvBusinessTripList.isEmpty()) {
            log.error("[ApprovalService] Error: ApvBusinessTrip not found with empNo {}", empNo);
            return null;
        }

        // DTO 객체를 저장할 빈 목록을 생성
        List<ApvBusinessTripDTO> apvBusinessTripDTOList = new ArrayList<>();

        // 엔터티 목록을 순회하고 각각의 엔터티를 DTO로 변환하여 목록에 추가
        for (ApvBusinessTrip apvBusinessTrip : apvBusinessTripList) {
            ApvBusinessTripDTO apvBusinessTripDTO = modelMapper.map(apvBusinessTrip, ApvBusinessTripDTO.class);
            apvBusinessTripDTOList.add(apvBusinessTripDTO);
        }
        System.out.println("apvBusinessTripDTOList = " + apvBusinessTripDTOList);
        log.info("[ApprovalService] Biz3-searchApvFormWithLines --------------- 출장신청서 조회  end");

        // DTO 객체들의 목록을 반환
        return apvBusinessTripDTOList;
    }
}

