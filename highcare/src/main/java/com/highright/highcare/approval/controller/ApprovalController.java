package com.highright.highcare.approval.controller;

import com.highright.highcare.approval.dto.*;
import com.highright.highcare.approval.repository.ApvFormRepository;
import com.highright.highcare.approval.service.ApprovalBizService;
import com.highright.highcare.approval.service.ApprovalExpService;
import com.highright.highcare.approval.service.ApprovalHrmService;
import com.highright.highcare.approval.service.ApprovalService;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.common.PageDTO;
import com.highright.highcare.common.PagingResponseDTO;
import com.highright.highcare.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/approval")
@CrossOrigin(origins = "http://highcare.coffit.today:3000")
@Slf4j
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;
    private final ApprovalBizService approvalBizService;
    private final ApprovalExpService approvalExpService;
    private final ApprovalHrmService approvalHrmService;
    private final ApvFormRepository apvFormRepository;


    /* Apv메인페이지 - 조건별 현황 1 */
    @Operation(summary = "전자결재 메인페이지", description = "전자결재 메인페이지에 접속", tags = {"ApprovalController"})
    @GetMapping("/main")
    public ResponseEntity<ResponseDTO> selectWriteApv(@RequestParam int empNo) {

        Map<String, Integer> countsMap = approvalService.selectApvMainCount(empNo);

        System.out.println("countsMap = " + countsMap);

        if (countsMap.isEmpty()) {
            return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK.value(), "조회결과없음"));
        }

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "작성 기안 상태 조회 성공", countsMap));
    }

    /* Apv메인페이지 - 리스트 */
    @Operation(summary = "최근 기안 양식 리스트 조회", description = "최근에 상신한 결재 양식을 조회합니다.", tags = {"ApprovalController"})
    @GetMapping("/apvList")
    public ResponseEntity<ResponseDTO> selectMyApvList(@RequestParam int empNo) {

//        List<ApvFormMainDTO> myApvList = approvalService.selectMyApvList(empNo);
        List<String> myApvList = approvalService.selectMyApvList(empNo);
        System.out.println("myApvList = " + myApvList);

        if (myApvList.isEmpty()) {
            return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK.value(), "조회결과없음"));
        }

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "작성 기안 상태 조회 성공", myApvList));
    }

    /* 전자결재 결재함 조회 */
    @Operation(summary = "결재함 조회", description = "조건별로 상신한 결재문서를 조회합니다.", tags = {"ApprovalController"})
    @GetMapping("/write")
    public ResponseEntity<ResponseDTO> selectWriteApvStatusApv(@RequestParam int empNo, @RequestParam String apvStatus
            , @RequestParam(name = "offset", defaultValue = "1") String offset) {

        log.info("[ProductController] selectListWithPaging => offset : {} ", offset);

        int total = approvalService.selectApvStatusTotal(empNo, apvStatus);
        Criteria criteria = new Criteria(Integer.valueOf(offset), 15);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(approvalService.selectListWithPaging(empNo, apvStatus, criteria));
        pagingResponseDTO.setPageInfo(new PageDTO(criteria, total));

        List<ApvFormMainDTO> writeApvStatusApvList = approvalService.selectWriteApvStatusApvList(empNo, apvStatus);

        if (writeApvStatusApvList.isEmpty()) {
            return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK.value(), "조회결과없음"));
        }

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "작성 기안 상태 조회 성공", writeApvStatusApvList));
    }

    /* 전자결재 수신함 조회 */
    @Operation(summary = "수신함 조회", description = "수신한 결재문서를 조회합니다.", tags = {"ApprovalController"})
    @GetMapping("/receive")
    public ResponseEntity<ResponseDTO> selectReceiveApvStatusApv(@RequestParam int empNo, @RequestParam String apvStatus
            , @RequestParam(name = "offset", defaultValue = "1") String offset) {

        log.info("[ProductController] selectReceiveApvStatusApv => offset : {} ", offset);

        int total = approvalService.selectApvStatusTotal(empNo, apvStatus);
        Criteria criteria = new Criteria(Integer.valueOf(offset), 15);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(approvalService.selectListWithPaging(empNo, apvStatus, criteria));
        pagingResponseDTO.setPageInfo(new PageDTO(criteria, total));

        List<ApvFormMainDTO> receiveApvStatusApvList = approvalService.selectReceiveApvStatusApvList(empNo, apvStatus);

        if (receiveApvStatusApvList.isEmpty()) {
            return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK.value(), "조회결과없음"));
        }

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "작성 기안 상태 조회 성공", receiveApvStatusApvList));
    }

    /* 첨부파일 다운로드 */
    @Operation(summary = "결재 첨부파일 다운로드", description = "결재문서의 첨부파일을 다운도르합니다.", tags = {"ApprovalController"})
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileData = approvalService.downloadFileData(fileName);

            if (fileData != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(fileData);
            } else {
                return ResponseEntity
                        .ok()
                        .body(new byte[0]); // 빈 데이터를 반환하거나 예외 처리
            }
        } catch (Exception ex) {
            // 예외 처리
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new byte[0]); // 빈 데이터를 반환하거나 예외 처리
        }
    }


    int statusCode;
    String responseMessage;

    /* 전자결재 승인 + 반려*/
    @Operation(summary = "전자결재 상태 변경", description = "결재문서의 결재 상태를 변경합니다. (승인, 반려)", tags = {"ApprovalController"})
    @PutMapping("/put/line/{apvLineNo}")
    public ResponseEntity<ResponseDTO> updateApprovalStatus(@PathVariable Long apvLineNo, @RequestParam Long apvNo, @RequestParam String apvStatus) {

        System.out.println("apvLineNo = " + apvLineNo);
        System.out.println("apvNo = " + apvNo);
        System.out.println("apvStatus = " + apvStatus);
        boolean updatedResponse = false;

        if(apvStatus == null || apvStatus.isEmpty()) {
            updatedResponse = approvalService.updateApprovalStatus(apvLineNo, apvNo);
        } else {
            updatedResponse = approvalService.updateApvStatusReject(apvNo);
        }

        if (!updatedResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, updatedResponse));

    }

    // 기안 취소(삭제)
    @Operation(summary = "기안 상신 취소(삭제)", description = "상신한 결재문서를 취소(삭제)합니다.", tags = {"ApprovalController"})
    @DeleteMapping("/delete/{apvNo}")
    public ResponseEntity<ResponseDTO> deleteApvForm(@PathVariable Long apvNo) {

        Boolean serviceResponse = approvalService.deleteApvForm(apvNo);

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }

    // 기안 조회
    @Operation(summary = "결재문서 조회", description = "상신한 결재문서를 조회합니다.", tags = {"ApprovalController"})
    @GetMapping("/search/{apvNo}")
    public ResponseEntity<?> searchApvFormWithLines(@PathVariable Long apvNo) {
        System.out.println("biz1View searchApvFormWithLines = " + apvNo);

        ApvFormDTO serviceResponse = approvalService.searchApvFormWithLines(apvNo);

        if (serviceResponse == null) {
            statusCode = HttpStatus.NOT_FOUND.value();
            responseMessage = "ApvForm not found with apvNo: " + apvNo;
            return ResponseEntity
                    .status(statusCode)
                    .body(new ResponseDTO(statusCode, responseMessage, null));
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "조회 성공";
            return ResponseEntity
                    .status(statusCode)
                    .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
        }
    }

    // 기안 수정
    @Operation(summary = "결재문서 수정", description = "상신한 결재문서를 수정합니다.", tags = {"ApprovalController"})
    @PutMapping(value = "/put/{apvNo}", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO> putApvFormWithLines(
            @PathVariable Long apvNo,
            @RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
            @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
            @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
            @RequestPart(value = "apvFileDTO", required = false) List<MultipartFile> apvFileDTO) {

        Boolean serviceResponse = false;
        String title = apvFormRepository.findTitleByApvNo(apvNo);
        System.out.println("title = " + title);
        switch (title) {
            case "회의록":
                serviceResponse = approvalBizService.updateApvMeetingLog(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "출장신청서":
                serviceResponse = approvalBizService.updateApvBusinessTrip(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "공문":
                serviceResponse = approvalBizService.updateApvOfficial(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "지출결의서" :
                serviceResponse = approvalExpService.updateApvExpense(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "출장경비신청서" :
                serviceResponse = approvalExpService.updateApvExpense(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "경조금신청서" :
                serviceResponse = approvalExpService.updateFamilyEvent(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "법인카드사용보고서" :
                serviceResponse = approvalExpService.updateApvCorpCard(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "연차신청서" :
                serviceResponse = approvalHrmService.updateApvVacation(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "기타휴가신청서" :
                serviceResponse = approvalHrmService.updateApvVacation(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
            case "서류발급신청서" :
                serviceResponse = approvalHrmService.updateApvIssuance(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                break;
                default:
                    System.out.println("기안서로 이동");
                    serviceResponse = approvalBizService.updateApvForm(apvNo, apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
                    break;
            }

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }



    /* 전자결재 - 업무 : biz1 기안서 */
    @Operation(summary = "결재상신 (업무:기안서)", description = "결재를 상신합니다.(업무:기안서)", tags = {"ApprovalController"})
    @PostMapping(value = "/insert/biz1")
    public ResponseEntity<ResponseDTO> insertApvFormWithLines(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                            @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                            @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                            List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        // 서비스 메서드를 호출하여 ApvFormWithLinesDTO 및 파일 정보를 등록
        Boolean serviceResponse = approvalBizService.insertApvFormWithLines(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }


    /* 전자결재 - 업무 : biz2 회의록 */
    @Operation(summary = "결재상신 (업무:회의록)", description = "결재를 상신합니다.(업무:회의록)", tags = {"ApprovalController"})
    @PostMapping(value = "/insert/biz2")
    public ResponseEntity<ResponseDTO> insertApvMeetingLog(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                           @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                           @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                           List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalBizService.insertApvMeetingLog(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
        int statusCode;
        String responseMessage;

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }


    /* 전자결재 - 업무 : biz3 출장신청서 */
    @Operation(summary = "결재상신 (업무:출장신청서)", description = "결재를 상신합니다.(업무:출장신청서)", tags = {"ApprovalController"})
    @PostMapping(value ="/insert/biz3")
    public ResponseEntity<ResponseDTO> insertApvBusinessTrip(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                             @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                             @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                             List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalBizService.insertApvBusinessTrip(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
        int statusCode;
        String responseMessage;

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }


    /* 전자결재 - 업무 : biz4 공문 */
    @Operation(summary = "결재상신 (업무:공문)", description = "결재를 상신합니다.(업무:공문)", tags = {"ApprovalController"})
    @PostMapping(value = "/insert/biz4")
    public ResponseEntity<ResponseDTO> insertApvOfficial(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                         @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                         @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                         List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalBizService.insertApvOfficial(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
        int statusCode;
        String responseMessage;

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }


    //    /* 전자결재 - 지출 : exp1 지출결의서 */
    @Operation(summary = "결재상신 (지출:지출결의서)", description = "결재를 상신합니다.(지출:지출결의서)", tags = {"ApprovalController"})
    @PostMapping(value ="/insert/exp1")
    public ResponseEntity<ResponseDTO> insertApvExpense(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                        @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                        @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                        List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalExpService.insertApvExpense(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
        int statusCode;
        String responseMessage;

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }

    /* 전자결재 - 지출 : exp4 출장경비정산서 */
    @Operation(summary = "결재상신(조회) (지출:출장경비정산서)", description = "결재를 상신을 위한 사전정보를 조회합니다.(지출:출장경비정산서)", tags = {"ApprovalController"})
    @GetMapping(value ="/search/exp4/{empNo}")
    public ResponseEntity<ResponseDTO> selectApvBusinessTrip(@PathVariable int empNo) {
        System.out.println("exp4-selectApvBusinessTrip = " + empNo);

        List<ApvBusinessTripDTO> serviceResponse = approvalBizService.selectApvBusinessTrip(empNo);

        if (serviceResponse == null || serviceResponse.isEmpty()) {
            statusCode = HttpStatus.NOT_FOUND.value();
            responseMessage = "ApvForm not found with empNo: " + empNo;
            return ResponseEntity
                    .status(statusCode)
                    .body(new ResponseDTO(statusCode, responseMessage, null));
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "조회 성공";
            return ResponseEntity
                    .status(statusCode)
                    .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
        }
    }


    @Operation(summary = "결재상신 (지출:출장경비정산서)", description = "결재를 상신합니다.(지출:출장경비정산서)", tags = {"ApprovalController"})
    @PostMapping(value ="/insert/exp4")
    public ResponseEntity<ResponseDTO> insertApvBusinessTripExp(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                                @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                                @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                                List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalExpService.insertApvExpense(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
        int statusCode;
        String responseMessage;

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }


    /* 전자결재 - 지출 : exp6 경조금신청서 */
    @Operation(summary = "결재상신 (지출:경조금신청서)", description = "결재를 상신합니다.(지출:경조금신청서)", tags = {"ApprovalController"})
    @PostMapping(value="/insert/exp6")
    public ResponseEntity<ResponseDTO> insertApvFamilyEvent(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                            @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                            @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                            List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalExpService.insertApvFamilyEvent(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
        int statusCode;
        String responseMessage;

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }

    /* 전자결재 - 지출 : exp7 법인카드사용보고서 */
    @Operation(summary = "결재상신 (지출:법인카드사용보고서)", description = "결재를 상신합니다.(지출:법인카드사용보고서)", tags = {"ApprovalController"})
    @PostMapping(value ="/insert/exp7")
    public ResponseEntity<ResponseDTO> insertApvCorpCard(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                         @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                         @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                         List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalExpService.insertApvCorpCard(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);
        int statusCode;
        String responseMessage;

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }


    /* 전자결재 - 인사 : hrm1 연차신청서, hrm2 기타휴가신청서 */
    @Operation(summary = "결재상신 (인사:기타휴가신청서)", description = "결재를 상신합니다.(인사:기타휴가신청서)", tags = {"ApprovalController"})
    @PostMapping(value = "/insert/hrm1")
    public ResponseEntity<ResponseDTO> insertApvVacation(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                         @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                         @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                         List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        // 서비스 메서드를 호출하여  ApvFormDTO, ApvLineDTOs, 파일 정보를 등록
        Boolean serviceResponse = approvalHrmService.insertApvVacation(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }

    /* 전자결재 - 인사 : hrm3 서류발급신청서 */
    @Operation(summary = "결재상신 (인사:서류발급신청서)", description = "결재를 상신합니다.(인사:서류발급신청서)", tags = {"ApprovalController"})
    @PostMapping(value ="/insert/hrm3")
    public ResponseEntity<ResponseDTO> insertApvIssuance(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                         @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                         @RequestPart("apvRefLineDTOs") List<ApvLineDTO> apvRefLineDTOs,
                                                         List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormDTO = " + apvFormDTO);
        System.out.println("biz1 apvLineDTOs = " + apvLineDTOs);
        System.out.println("biz1 apvRefLineDTOs = " + apvRefLineDTOs);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        Boolean serviceResponse = approvalHrmService.insertApvIssuance(apvFormDTO, apvLineDTOs, apvRefLineDTOs, apvFileDTO);

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "상신 등록 실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "상신 등록 성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }

}