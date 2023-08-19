package com.highright.highcare.approval.controller;

import com.highright.highcare.approval.dto.ApvFormDTO;
import com.highright.highcare.approval.service.ApprovalService;
import com.highright.highcare.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approval")
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseDTO> insertApv(@RequestBody ApvFormDTO apvFormDTO){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "상신 등록 성공", approvalService.insertApvForm(apvFormDTO)));
    }


    @GetMapping("/write/{empNo}")
    public ResponseEntity<ResponseDTO> selectWriteApv(@PathVariable int empNo){
        List<ApvFormDTO> writeApvList = approvalService.selectWriteApvList(empNo);

            if(writeApvList.isEmpty()){
                return ResponseEntity
                        .ok()
                        .body(new ResponseDTO(HttpStatus.OK.value(),  "조회결과없음"));
            }

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(),  "작성 기안 조회 성공" , writeApvList));
    }




}
