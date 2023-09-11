package com.highright.highcare.approval.controller;

import com.highright.highcare.approval.dto.*;
import com.highright.highcare.approval.entity.ApvForm;
import com.highright.highcare.approval.service.ApprovalBizService;
import com.highright.highcare.approval.service.ApprovalExpService;
import com.highright.highcare.approval.service.ApprovalHrmService;
import com.highright.highcare.approval.service.ApprovalService;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.common.PageDTO;
import com.highright.highcare.common.PagingResponseDTO;
import com.highright.highcare.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/approval")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;
    private final ApprovalBizService approvalBizService;
    private final ApprovalExpService approvalExpService;
    private final ApprovalHrmService approvalHrmService;


    @Autowired
    public ApprovalController(
            ApprovalService approvalService,
            ApprovalBizService approvalBizService,
            ApprovalExpService approvalExpService,
            ApprovalHrmService approvalHrmService
    ) {
        this.approvalService = approvalService;
        this.approvalBizService = approvalBizService;
        this.approvalExpService = approvalExpService;
        this.approvalHrmService = approvalHrmService;

    }


    /* Apv메인페이지 - 조건별 현황 1 */
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
    @GetMapping("/apvList")
    public ResponseEntity<ResponseDTO> selectMyApvList(@RequestParam int empNo) {

        List<ApvFormMainDTO> myApvList = approvalService.selectMyApvList(empNo);
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


    int statusCode;
    String responseMessage;


    /* 전자결재 승인 + 반려*/
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


    /* 전자결재 - 업무 : biz1 기안서 */
    @PostMapping("/insert/biz1")
    public ResponseEntity<ResponseDTO> insertApvFormWithLines(@RequestPart("apvFormWithLinesDTO") ApvFormWithLinesDTO apvFormWithLinesDTO,
                                                              @RequestPart("apvFileDTOs") List<MultipartFile> apvFileDTO) {
        System.out.println("biz1 apvFormWithLinesDTO = " + apvFormWithLinesDTO);
        System.out.println("biz1 apvFileDTO = " + apvFileDTO);

        // 서비스 메서드를 호출하여 ApvFormWithLinesDTO 및 파일 정보를 등록
        Boolean serviceResponse = approvalBizService.insertApvFormWithLines(apvFormWithLinesDTO, apvFileDTO);

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

    @PutMapping("/put/biz1/{apvNo}")
    public ResponseEntity<ResponseDTO> putApvFormWithLines(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO) {
        System.out.println("biz1 apvFormWithLinesDTO = " + apvFormWithLinesDTO);

        Boolean serviceResponse = approvalBizService.putApvFormWithLines(apvFormWithLinesDTO);

        if (!serviceResponse) {
            statusCode = HttpStatus.BAD_REQUEST.value();
            responseMessage = "실패";
        } else {
            statusCode = HttpStatus.OK.value();
            responseMessage = "성공";
        }
        return ResponseEntity
                .status(statusCode)
                .body(new ResponseDTO(statusCode, responseMessage, serviceResponse));
    }



    /* 전자결재 - 업무 : biz2 회의록 */
    @PostMapping("/insert/biz2")
    public ResponseEntity<ResponseDTO> insertApvMeetingLog(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO) {
        System.out.println("biz2 apvFormWithLinesDTO = " + apvFormWithLinesDTO);

        Boolean serviceResponse = approvalBizService.insertApvMeetingLog(apvFormWithLinesDTO);

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
    @PostMapping("/insert/biz3")
    public ResponseEntity<ResponseDTO> insertApvBusinessTrip(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO) {

        Boolean serviceResponse = approvalBizService.insertApvBusinessTrip(apvFormWithLinesDTO);
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
    @PostMapping("/insert/exp1")
    public ResponseEntity<ResponseDTO> insertApvExpense(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO){

        Boolean serviceResponse = approvalExpService.insertApvExpense(apvFormWithLinesDTO);
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


    /* 전자결재 - 지출 : exp2 지출결의서 */
    @PostMapping("/insert/exp2")
    public ResponseEntity<ResponseDTO> insertApvExpense2(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "상신 등록 성공", approvalService.insertApvExpense(apvFormWithLinesDTO)));
    }



    /* 전자결재 - 지출 : exp4 출장경비정산서 */
    @GetMapping("/search/exp4/{empNo}")
    public ResponseEntity<ResponseDTO> selectApvBusinessTrip(@PathVariable int empNo) {
        System.out.println("exp4-searchApvFormWithLines = " + empNo);

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


    @PostMapping("/insert/exp4")
    public ResponseEntity<ResponseDTO> insertApvBusinessTripExp(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO){

        Boolean serviceResponse = approvalExpService.insertApvExpense(apvFormWithLinesDTO);
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


    /* 전자결재 - 지출 : exp6 경조금 신청서 */
    @PostMapping("/insert/exp6")
    public ResponseEntity<ResponseDTO> insertApvFamilyEvent(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO){

        Boolean serviceResponse = approvalExpService.insertApvFamilyEvent(apvFormWithLinesDTO);
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
    @PostMapping("/insert/exp7")
    public ResponseEntity<ResponseDTO> insertApvCorpCard(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO){

        Boolean serviceResponse = approvalExpService.insertApvCorpCard(apvFormWithLinesDTO);
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
    @PostMapping("/insert/hrm1")
    public ResponseEntity<ResponseDTO> insertApvVacation(@RequestPart("apvFormDTO") ApvFormDTO apvFormDTO,
                                                         @RequestPart("apvLineDTOs") List<ApvLineDTO> apvLineDTOs,
                                                         @RequestPart("apvFileDTOs") List<MultipartFile> apvFileDTO) {
        System.out.println("hrm1 apvFormWithLinesDTO = " + apvFormDTO);
        System.out.println("hrm1 apvFormWithLinesDTO = " + apvLineDTOs);
        System.out.println("hrm1 apvFileDTO = " + apvFileDTO);

        // 서비스 메서드를 호출하여 ApvFormWithLinesDTO 및 파일 정보를 등록
        Boolean serviceResponse = approvalHrmService.insertApvVacation(apvFormDTO, apvLineDTOs, apvFileDTO);

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
    @PostMapping("/insert/hrm3")
    public ResponseEntity<ResponseDTO> insertApvIssuance(@RequestBody ApvFormWithLinesDTO apvFormWithLinesDTO){
        System.out.println("hrm3 apvFormWithLinesDTO = " + apvFormWithLinesDTO);

        Boolean serviceResponse = approvalHrmService.insertApvIssuance(apvFormWithLinesDTO);

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