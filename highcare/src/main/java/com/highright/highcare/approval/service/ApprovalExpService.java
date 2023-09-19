package com.highright.highcare.approval.service;

import com.highright.highcare.approval.dto.*;
import com.highright.highcare.approval.entity.*;
import com.highright.highcare.approval.repository.*;
import com.highright.highcare.common.Criteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalExpService {

    private final ModelMapper modelMapper;
    private final ApprovalService approvalService;
    private final ApvFormMainRepository apvFormMainRepository;
    private final ApvFormRepository apvFormRepository;
    private final ApvExpFormRepository apvExpFormRepository;
    private final ApvFamilyEventRepository apvFamilyEventRepository;
    private final ApvCorpCardRepository apvCorpCardRepository;


    /*
     * Exp1 : 지출결의서
     * Exp2 : 지출결의서2
     * Exp4 : 출장경비정산서
     * Exp6 : 경조금신청서
     * Exp7 : 법인카드사용보고서
     * */


    /* 전자결재 - 지출 : Exp1 지출결의서, Exp4 출장경비정산서 */
    @Transactional
    public Boolean insertApvExpense(ApvFormDTO apvFormDTO,
                                    List<ApvLineDTO> apvLineDTOs,
                                    List<ApvLineDTO> apvRefLineDTOs,
                                    List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Exp1 insertApvExpense --------------- 지출결의서 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvRefLineDTOs {}", apvRefLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            List<ApvExpFormDTO> apvExpFormDTOList = apvFormDTO.getApvExpForms();

            // ApvForm 및 ApvFormMain 생성 및 저장
            ApvFormMain apvFormMain = modelMapper.map(apvFormDTO, ApvFormMain.class);
            apvFormMain = apvFormMainRepository.save(apvFormMain);
            Long apvNo = apvFormMain.getApvNo();

            // ApvExpFormDTO를 ApvExpForm 엔티티로 매핑하고 ApvNo 설정
            List<ApvExpForm> apvExpFormList = apvExpFormDTOList.stream()
                    .map(apvExpFormDTO -> {
                        ApvExpForm apvExpForm = modelMapper.map(apvExpFormDTO, ApvExpForm.class);
                        apvExpForm.setApvNo(apvNo);
                        return apvExpForm;
                    })
                    .collect(Collectors.toList());

            // ApvExpForm 엔티티 저장
            apvExpFormList = apvExpFormRepository.saveAll(apvExpFormList);

            approvalService.insertApprovalCommon(apvFormMain, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Exp1 insertApvExpense --------------- 지출결의서 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] 오류 발생 - Exp1 insertApvExpense : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 지출 : Exp1 지출결의서, Exp4 출장경비정산서 수정*/
    @Transactional
    public Boolean updateApvExpense(Long apvNo,
                                    ApvFormDTO apvFormDTO,
                                    List<ApvLineDTO> apvLineDTOs,
                                    List<ApvLineDTO> apvRefLineDTOs,
                                    List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Exp1, Exp4 updateApvExpense --------------- 지출결의서 업데이트 start");
        try {
            // 기존 ApvForm을 검색
            ApvForm savedApvForm = apvFormRepository.findById(apvNo).orElse(null);
            ApvFormMain savedApvFormMain = apvFormMainRepository.findById(apvNo).orElse(null);

            approvalService.updateApprovalCommon(apvNo, savedApvForm, savedApvFormMain, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            apvExpFormRepository.deleteByApvNo(apvNo);

            List<ApvExpFormDTO> apvExpFormDTO = apvFormDTO.getApvExpForms();
            // ApvExpFormDTO ApvExpForm 엔티티로 매핑하고 ApvNo를 설정
            List<ApvExpForm> apvExpFormList = apvExpFormDTO.stream()
                    .map(dto -> {
                        ApvExpForm apvExpForm = modelMapper.map(dto, ApvExpForm.class);
                        apvExpForm.setApvNo(apvNo);
                        return apvExpForm;
                    })
                    .collect(Collectors.toList());

            // ApvExpForm 엔티티를 저장
            apvExpFormList = apvExpFormRepository.saveAll(apvExpFormList);

            log.info("[ApprovalService] Exp1, Exp4 updateApvExpense --------------- 지출결의서 업데이트 end");
            return true;
        } catch(Exception e){
            log.error("[ApprovalService] 오류 발생 - Exp1, Exp4 updateApvExpense : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 지출 : Exp6 경조금신청서 */
    @Transactional
    public Boolean insertApvFamilyEvent(ApvFormDTO apvFormDTO,
                                        List<ApvLineDTO> apvLineDTOs,
                                        List<ApvLineDTO> apvRefLineDTOs,
                                        List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Exp6 insertApvFamilyEvent --------------- 경조금신청서 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvRefLineDTOs {}", apvRefLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            List<ApvFamilyEventDTO> apvFamilyEventDTO = apvFormDTO.getApvFamilyEvents();

            // ApvFormMain을 저장하고 생성된 ApvNo를 가져오기
            ApvFormMain apvFormMain = modelMapper.map(apvFormDTO, ApvFormMain.class);
            apvFormMain = apvFormMainRepository.save(apvFormMain);
            Long apvNo = apvFormMain.getApvNo();

            // ApvFamilyEventDTO를 ApvFamilyEvent 엔티티로 매핑하고 ApvNo를 설정
            List<ApvFamilyEvent> apvFamilyEventList = apvFamilyEventDTO.stream()
                    .map(dto -> {
                        ApvFamilyEvent apvFamilyEvent = modelMapper.map(dto, ApvFamilyEvent.class);
                        apvFamilyEvent.setApvNo(apvNo);
                        return apvFamilyEvent;
                    })
                    .collect(Collectors.toList());

            // ApvFamilyEvent 엔티티를 저장
            apvFamilyEventList = apvFamilyEventRepository.saveAll(apvFamilyEventList);

            approvalService.insertApprovalCommon(apvFormMain, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Exp6 insertApvFamilyEvent --------------- 경조금신청서 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Exp6 insertApvFamilyEvent : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 지출 : Exp6 경조금신청서 수정*/
    @Transactional
    public Boolean updateFamilyEvent(Long apvNo,
                                     ApvFormDTO apvFormDTO,
                                     List<ApvLineDTO> apvLineDTOs,
                                     List<ApvLineDTO> apvRefLineDTOs,
                                     List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Exp6 updateFamilyEvent --------------- 경조금신청서 업데이트 start");
        try {
            // 기존 ApvForm을 검색
            ApvForm savedApvForm = apvFormRepository.findById(apvNo).orElse(null);
            ApvFormMain savedApvFormMain = apvFormMainRepository.findById(apvNo).orElse(null);

            approvalService.updateApprovalCommon(apvNo, savedApvForm, savedApvFormMain, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            apvFamilyEventRepository.deleteByApvNo(apvNo);

            List<ApvFamilyEventDTO> apvFamilyEventDTO = apvFormDTO.getApvFamilyEvents();
            // ApvFamilyEventDTO를 ApvFamilyEvent 엔티티로 매핑하고 ApvNo를 설정
            List<ApvFamilyEvent> apvFamilyEventList = apvFamilyEventDTO.stream()
                    .map(dto -> {
                        ApvFamilyEvent apvFamilyEvent = modelMapper.map(dto, ApvFamilyEvent.class);
                        apvFamilyEvent.setApvNo(apvNo);
                        return apvFamilyEvent;
                    })
                    .collect(Collectors.toList());

            // ApvFamilyEvent 엔티티를 저장
            apvFamilyEventList = apvFamilyEventRepository.saveAll(apvFamilyEventList);

            log.info("[ApprovalService] Exp6 updateFamilyEvent --------------- 경조금신청서 업데이트 end");
            return true;
        } catch(Exception e){
            log.error("[ApprovalService] 오류 발생 - Exp6 updateFamilyEvent : " + e.getMessage());
            return false;
        }
    }


    /* 전자결재 - 지출 : Exp7 법인카드사용보고서 */
    @Transactional
    public Boolean insertApvCorpCard(ApvFormDTO apvFormDTO,
                                     List<ApvLineDTO> apvLineDTOs,
                                     List<ApvLineDTO> apvRefLineDTOs,
                                     List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Exp7 insertApvCorpCard --------------- 법인카드사용보고서 상신 start ");
        log.info("[ApprovalService] apvFormDTO {}", apvFormDTO);
        log.info("[ApprovalService] apvLineDTOs {}", apvLineDTOs);
        log.info("[ApprovalService] apvRefLineDTOs {}", apvRefLineDTOs);
        log.info("[ApprovalService] apvFileDTO {}", apvFileDTO);

        try {
            List<ApvCorpCardDTO> apvCorpCardDTO = apvFormDTO.getApvCorpCards();

            // ApvFormMain과 ApvForm 생성 및 저장
            ApvForm apvForm = modelMapper.map(apvFormDTO, ApvForm.class);
            ApvFormMain apvFormMain = modelMapper.map(apvFormDTO, ApvFormMain.class);
            apvFormMain = apvFormMainRepository.save(apvFormMain);
            Long apvNo = apvFormMain.getApvNo();

            // ApvCorpCardDTO를 ApvCorpCard 엔티티로 매핑하고 ApvNo 설정
            List<ApvCorpCard> apvCorpCardList = apvCorpCardDTO.stream()
                    .map(dto -> {
                        ApvCorpCard apvCorpCard = modelMapper.map(dto, ApvCorpCard.class);
                        apvCorpCard.setApvNo(apvNo);
                        return apvCorpCard;
                    })
                    .collect(Collectors.toList());

            // ApvCorpCard 엔티티를 저장
            apvCorpCardList = apvCorpCardRepository.saveAll(apvCorpCardList);

            approvalService.insertApprovalCommon(apvFormMain, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            log.info("[ApprovalService] Exp7 insertApvCorpCard --------------- 법인카드사용보고서 상신 end ");
            return true;
        } catch (Exception e) {
            log.error("[ApprovalService] Error Exp7 insertApvCorpCard : " + e.getMessage());
            return false;
        }
    }

    /* 전자결재 - 지출 :Exp7 법인카드사용보고서 수정*/
    @Transactional
    public Boolean updateApvCorpCard(Long apvNo,
                                     ApvFormDTO apvFormDTO,
                                     List<ApvLineDTO> apvLineDTOs,
                                     List<ApvLineDTO> apvRefLineDTOs,
                                     List<MultipartFile> apvFileDTO) {
        log.info("[ApprovalService] Exp7 updateApvCorpCard --------------- 법인카드사용보고서 업데이트 start");
        try {
            // 기존 ApvForm을 검색
            ApvForm savedApvForm = apvFormRepository.findById(apvNo).orElse(null);
            ApvFormMain savedApvFormMain = apvFormMainRepository.findById(apvNo).orElse(null);

            approvalService.updateApprovalCommon(apvNo, savedApvForm, savedApvFormMain, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

            apvCorpCardRepository.deleteByApvNo(apvNo);

            List<ApvCorpCardDTO> apvCorpCardDTO = apvFormDTO.getApvCorpCards();
            // ApvCorpCardDTO를 ApvCorpCard 엔티티로 매핑하고 ApvNo를 설정
            List<ApvCorpCard> apvCorpCardList = apvCorpCardDTO.stream()
                    .map(dto -> {
                        ApvCorpCard apvCorpCard = modelMapper.map(dto, ApvCorpCard.class);
                        apvCorpCard.setApvNo(apvNo);
                        return apvCorpCard;
                    })
                    .collect(Collectors.toList());

            // ApvCorpCard 엔티티를 저장
            apvCorpCardList = apvCorpCardRepository.saveAll(apvCorpCardList);

            log.info("[ApprovalService] Exp7 updateApvCorpCard --------------- 법인카드사용보고서 업데이트 end");
            return true;
        } catch(Exception e){
            log.error("[ApprovalService] 오류 발생 - Exp7 updateApvCorpCard : " + e.getMessage());
            return false;
        }
    }

}