package com.highright.highcare.approval.service;

import com.highright.highcare.approval.dto.*;
import com.highright.highcare.approval.entity.*;
import com.highright.highcare.approval.repository.*;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.pm.entity.PmEmployee;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ApprovalBizService {

    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;
    private final ApvFormMainRepository apvFormMainRepository;
    private final ApvFormRepository apvFormRepository;
    private final ApvMeetingLogRepository apvMeetingLogRepository;
    private final ApvBusinessTripRepository apvBusinessTripRepository;
    private final ApvOfficialRepository apvOfficialRepository;


    /*
    * Biz1 : 기본 기안서
    * Biz2 : 회의록
    * Biz3 : 출장신청서
    * Biz4 : 공문
    * */


    /* 전자결재 - 업무: Biz1 기안서 */
    @Transactional
    public Boolean insertApvFormWithLines(ApvFormDTO apvFormDTO,
                                          List<ApvLineDTO> apvLineDTOs,
                                          List<ApvLineDTO> apvRefLineDTOs,
                                          List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Biz1-insertApvForm --------------- 기안서 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvRefLineDTOs {}", apvRefLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            // ApvForm 생성 및 저장
            ApvForm apvForm = modelMapper.map(apvFormDTO, ApvForm.class);
            ApvForm savedApvForm = apvFormRepository.save(apvForm);

            approvalService.insertApprovalCommon(savedApvForm, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Biz1-insertApvForm --------------- 기안서 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error - Biz1-insertApvForm : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무: Biz1 기안서 수정 */
    @Transactional
    public Boolean updateApvForm(Long apvNo,
                                 ApvFormDTO apvFormDTO,
                                 List<ApvLineDTO> apvLineDTOs,
                                 List<ApvLineDTO> apvRefLineDTOs,
                                 List<MultipartFile> apvFileDTO) {

        log.info("[ApprovalService] Biz1 updateApvForm --------------- 문서 업데이트 start ");
        try {
            // 기존 ApvForm을 검색
            ApvForm savedApvForm = apvFormRepository.findById(apvNo).orElse(null);
            ApvFormMain savedApvFormMain = apvFormMainRepository.findById(apvNo).orElse(null);

            approvalService.updateApprovalCommon(apvNo, savedApvForm, savedApvFormMain, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Biz1 updateApvForm --------------- 문서 업데이트 end ");
            return true;
        } catch(Exception e){
            log.error("[ApprovalService] 오류 발생 - Biz1 updateApvForm : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무: Biz2 회의록 */
    @Transactional
    public Boolean insertApvMeetingLog(ApvFormDTO apvFormDTO,
                                       List<ApvLineDTO> apvLineDTOs,
                                       List<ApvLineDTO> apvRefLineDTOs,
                                       List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalHrmService] Biz2-insertApvMeetingLog --------------- 회의록 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvRefLineDTOs {}", apvRefLineDTOs);
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
                        return apvMeetingLog;
                    })
                    .collect(Collectors.toList());

            // ApvMeetingLog 엔티티를 저장
            apvMeetingLogList = apvMeetingLogRepository.saveAll(apvMeetingLogList);

            approvalService.insertApprovalCommon(apvFormMain, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Biz2 insertApvMeetingLog --------------- 회의록 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Biz2 insertApvMeetingLog : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무: Biz2 회의록 수정*/
    @Transactional
    public Boolean updateApvMeetingLog(Long apvNo,
                                       ApvFormDTO apvFormDTO,
                                       List<ApvLineDTO> apvLineDTOs,
                                       List<ApvLineDTO> apvRefLineDTOs,
                                       List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Biz2 updateApvMeetingLog --------------- 회의록 업데이트 start ");
        try {
            // 기존 ApvForm을 검색
            ApvForm savedApvForm = apvFormRepository.findById(apvNo).orElse(null);
            ApvFormMain savedApvFormMain = apvFormMainRepository.findById(apvNo).orElse(null);

            approvalService.updateApprovalCommon(apvNo, savedApvForm, savedApvFormMain, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            apvMeetingLogRepository.deleteByApvNo(apvNo);

            List<ApvMeetingLogDTO> apvMeetingLogDTO = apvFormDTO.getApvMeetingLogs();
            // ApvMeetingLogDTO를 ApvMeetingLog 엔티티로 매핑하고 ApvNo를 설정
            List<ApvMeetingLog> apvMeetingLogList = apvMeetingLogDTO.stream()
                    .map(dto -> {
                        ApvMeetingLog apvMeetingLog = modelMapper.map(dto, ApvMeetingLog.class);
                        apvMeetingLog.setApvNo(apvNo);
                        return apvMeetingLog;
                    })
                    .collect(Collectors.toList());

            // ApvMeetingLog 엔티티를 저장
            apvMeetingLogList = apvMeetingLogRepository.saveAll(apvMeetingLogList);

            log.info("[ApprovalService] Biz2 updateApvMeetingLog --------------- 문서 업데이트 end ");
            return true;
        } catch(Exception e){
            log.error("[ApprovalService] 오류 발생 - Biz2 updateApvMeetingLog : " + e.getMessage());
            return false;
        }
    }


    /* 전자결재 - 업무 : Biz3 출장신청서 */
    @Transactional
    public Boolean insertApvBusinessTrip(ApvFormDTO apvFormDTO,
                                         List<ApvLineDTO> apvLineDTOs,
                                         List<ApvLineDTO> apvRefLineDTOs,
                                         List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalHrmService] Biz3-insertApvBusinessTrip --------------- 출장신청서 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvRefLineDTOs {}", apvRefLineDTOs);
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
                        return apvBusinessTrip;
                    })
                    .collect(Collectors.toList());

            // ApvBusinessTrip 엔티티를 저장
            apvBusinessTripList = apvBusinessTripRepository.saveAll(apvBusinessTripList);

            approvalService.insertApprovalCommon(apvFormMain, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Biz3 insertApvBusinessTrip --------------- 출장신청서 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Biz3 insertApvBusinessTrip : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무 : Biz3 출장신청서 조회 */
    public List<ApvBusinessTripDTO> selectApvBusinessTrip(int empNo) {
        log.info("[ApprovalService] Biz3-selectApvBusinessTrip --------------- 출장신청서 조회 start");

        List<ApvBusinessTrip> apvBusinessTripList = apvBusinessTripRepository.findByEmpNo(empNo);

        if (apvBusinessTripList == null || apvBusinessTripList.isEmpty()) {
            log.error("[ApprovalService] Error: selectApvBusinessTrip not found with empNo {}", empNo);
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
        log.info("[ApprovalService] Biz3-selectApvBusinessTrip --------------- 출장신청서 조회 end");

        // DTO 객체들의 목록을 반환
        return apvBusinessTripDTOList;
    }

    /* 전자결재 - 업무 : Biz3 출장신청서 수정*/
    @Transactional
    public Boolean updateApvBusinessTrip(Long apvNo,
                                         ApvFormDTO apvFormDTO,
                                         List<ApvLineDTO> apvLineDTOs,
                                         List<ApvLineDTO> apvRefLineDTOs,
                                         List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Biz3-updateApvBusinessTrip --------------- 출장신청서 업데이트 start");
        try {
            // 기존 ApvForm을 검색
            ApvForm savedApvForm = apvFormRepository.findById(apvNo).orElse(null);
            ApvFormMain savedApvFormMain = apvFormMainRepository.findById(apvNo).orElse(null);

            approvalService.updateApprovalCommon(apvNo, savedApvForm, savedApvFormMain, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            apvBusinessTripRepository.deleteByApvNo(apvNo);

            List<ApvBusinessTripDTO> apvBusinessTripDTO = apvFormDTO.getApvBusinessTrips();
            // ApvBusinessTripDTO를 ApvBusinessTrip 엔티티로 매핑하고 ApvNo를 설정
            List<ApvBusinessTrip> apvBusinessTripList = apvBusinessTripDTO.stream()
                    .map(dto -> {
                        ApvBusinessTrip apvBusinessTrip = modelMapper.map(dto, ApvBusinessTrip.class);
                        apvBusinessTrip.setApvNo(apvNo);
                        return apvBusinessTrip;
                    })
                    .collect(Collectors.toList());

            // ApvBusinessTrip 엔티티를 저장
            apvBusinessTripList = apvBusinessTripRepository.saveAll(apvBusinessTripList);

//            // 테이블에서 apvNo와 일치하는 데이터를 삭제합니다.
//            apvLineRepository.deleteByApvNo(apvNo);
//            apvFileRepository.deleteByApvNo(apvNo);
//
//            System.out.println("savedApvFormMain======================== = " + savedApvFormMain);
//            System.out.println("============================================================ 1-2");
//
//            // ApvLine 엔터티 업데이트
//            List<ApvLine> apvLineList = apvLineDTOs.stream()
//                    .map(dto -> {
//                        ApvLine apvLine = modelMapper.map(dto, ApvLine.class);
//                        apvLine.setApvNo(apvNo);
//                        log.info("apvLine = {}", apvLine);
//                        return apvLine;
//                    })
//                    .collect(Collectors.toList());
//            System.out.println("apvLineList ================================== " + apvLineList);
//            System.out.println("============================================================ 2");
//
//            // 첨부파일 등록을 위해 서비스로 DTO전달
//            List<ApvFile> apvFiles = new ArrayList<>();
//            if (apvFileDTO != null && !apvFileDTO.isEmpty()) {
//                apvFiles = approvalService.insertFiles(apvNo, apvFileDTO);
//            }
//
//            System.out.println("apvFiles ================================== " + apvFiles);
//            System.out.println("============================================================ 3");
//            // ApvLine, ApvFile 엔티티를 ApvFormMain에 설정
//            apvLineRepository.saveAll(apvLineList);
//            apvFileRepository.saveAll(apvFiles);
//            System.out.println("savedApvForm = " + savedApvForm);
//            System.out.println("============================================================ 4");
//
//            // 승인 상태를 확인하고 업데이트
//            if (apvLineRepository.apvNoAllApproved(apvNo) == 0) {
//                apvFormRepository.updateApvStatusToCompleted(apvNo);
//            }

            log.info("[ApprovalService] Biz3-updateApvBusinessTrip --------------- 출장신청서 업데이트 end");
            return true;
        } catch(Exception e){
            log.error("[ApprovalService] 오류 발생 - Biz3 updateApvBusinessTrip : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무 : Biz4 공문 */
    @Transactional
    public Boolean insertApvOfficial(ApvFormDTO apvFormDTO,
                                     List<ApvLineDTO> apvLineDTOs,
                                     List<ApvLineDTO> apvRefLineDTOs,
                                     List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalHrmService] Biz4-insertApvOfficial --------------- 공문 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvRefLineDTOs {}", apvRefLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            List<ApvOfficialDTO> apvOfficialDTO = apvFormDTO.getApvOfficials();

            // ApvFormMain을 저장하고 생성된 ApvNo를 가져오기
            ApvFormMain apvFormMain = modelMapper.map(apvFormDTO, ApvFormMain.class);
            apvFormMain = apvFormMainRepository.save(apvFormMain);
            Long apvNo = apvFormMain.getApvNo();

            // ApvMeetingLogDTO를 ApvMeetingLog 엔티티로 매핑하고 ApvNo를 설정
            List<ApvOfficial> apvOfficialList = apvOfficialDTO.stream()
                    .map(dto -> {
                        ApvOfficial apvOfficial = modelMapper.map(dto, ApvOfficial.class);
                        apvOfficial.setApvNo(apvNo);
                        return apvOfficial;
                    })
                    .collect(Collectors.toList());

            // ApvMeetingLog 엔티티를 저장
            apvOfficialList = apvOfficialRepository.saveAll(apvOfficialList);

            approvalService.insertApprovalCommon(apvFormMain, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Biz4 insertApvOfficial --------------- 공문 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Biz4 insertApvOfficial : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 업무 : Biz4 공문 조회 */
    public Boolean updateApvOfficial(Long apvNo,
                                     ApvFormDTO apvFormDTO,
                                     List<ApvLineDTO> apvLineDTOs,
                                     List<ApvLineDTO> apvRefLineDTOs,
                                     List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Biz4 updateApvOfficial --------------- 공문 업데이트 start ");
        try {
            // 기존 ApvForm을 검색
            ApvForm savedApvForm = apvFormRepository.findById(apvNo).orElse(null);
            ApvFormMain savedApvFormMain = apvFormMainRepository.findById(apvNo).orElse(null);

            approvalService.updateApprovalCommon(apvNo, savedApvForm, savedApvFormMain, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            apvOfficialRepository.deleteByApvNo(apvNo);

            List<ApvOfficialDTO> apvOfficialDTO = apvFormDTO.getApvOfficials();
            // ApvMeetingLogDTO를 ApvMeetingLog 엔티티로 매핑하고 ApvNo를 설정
            List<ApvOfficial> apvOfficialList = apvOfficialDTO.stream()
                    .map(dto -> {
                        ApvOfficial apvOfficial = modelMapper.map(dto, ApvOfficial.class);
                        apvOfficial.setApvNo(apvNo);
                        return apvOfficial;
                    })
                    .collect(Collectors.toList());

            // ApvMeetingLog 엔티티를 저장
            apvOfficialList = apvOfficialRepository.saveAll(apvOfficialList);

            log.info("[ApprovalService] Biz4 updateApvOfficial --------------- 공문 업데이트 end ");
            return true;
        } catch(Exception e){
            log.error("[ApprovalService] 오류 발생 - Biz4 updateApvOfficial : " + e.getMessage());
            return false;
        }
    }
}

