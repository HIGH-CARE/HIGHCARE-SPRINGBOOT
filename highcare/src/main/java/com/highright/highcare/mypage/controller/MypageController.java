package com.highright.highcare.mypage.controller;

import com.highright.highcare.auth.dto.LoginMemberDTO;
import com.highright.highcare.common.Criteria;
import com.highright.highcare.common.PageDTO;
import com.highright.highcare.common.PagingResponseDTO;
import com.highright.highcare.common.ResponseDTO;
import com.highright.highcare.mypage.Repository.ProfileRepository;
import com.highright.highcare.mypage.dto.*;

import com.highright.highcare.mypage.entity.MyAnnual;
import com.highright.highcare.mypage.entity.MyProfile;
import com.highright.highcare.mypage.entity.MyProfileFile;
import com.highright.highcare.mypage.service.MypageService;
import com.highright.highcare.pm.dto.ManagementDTO;
import com.highright.highcare.pm.entity.Management;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@Slf4j
public class MypageController {

    private final MypageService mypageService;


    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    @Operation(summary = "프로필, 프로필파일 조회페이지", description = "프로필페이지에 접속", tags = {"MypageController"})
    @GetMapping ("/profile/{empNo}")
    public ResponseEntity<ResponseDTO> selectProfile(@PathVariable int empNo) {

        List<MyProfileDTO> profilefileList = mypageService.selectProfilefileList(empNo);
        log.info("empNo [Controller] ================profilefileList{} ", profilefileList);

       if(profilefileList == null ){
            return ResponseEntity
                    .ok()
                    .body(new ResponseDTO(HttpStatus.OK.value(),  "조회결과없음"));
        }

        System.out.println("empNo [Controller] ========================== " + empNo);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "프로필 조회 성공", profilefileList));

    }

    @Operation(summary = "프로필사진 업데이트", description = "프로필페이지에 사진을 등록합니다.", tags = {"MypageController"})
    @PostMapping("/update")
    public ResponseEntity<ResponseDTO> updateMyProfileFile(@ModelAttribute MyProfileFileDTO myProfileFileDTO, MultipartFile profileImage){

        log.info("insertMyProfileFile multifile!!!!!!! {}", profileImage);


        MyProfileFileDTO updateMyProfile = mypageService.updateMyProfileFile(myProfileFileDTO, profileImage);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "사진 파일 등록 성공", updateMyProfile));

    }

    @Operation(summary = "연자 조회 페이지", description = "개인의 연차를 조회합니다.", tags = {"MypageController"})
    @GetMapping("/anselect/{empNo}")
    public ResponseEntity<ResponseDTO> annselect(@AuthenticationPrincipal LoginMemberDTO member,
                                                 @PathVariable int empNo
            , @RequestParam(name="offset", defaultValue = "1") String offset) {

        log.info("offset start===========");
        log.info("offset==== :{}", offset);


        int total = mypageService.annselectTotal(empNo);
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);


        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(mypageService.selectAnnListPaging(empNo, cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));



        log.info("Controller]  annselect pagingResponseDTO^^^^^^ {}", pagingResponseDTO);
        log.info("[Controller] annselect end=================");


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "연차 조회 성공", pagingResponseDTO));
    }


    @Operation(summary = "근태 조회 페이지", description = "개인별 근태를 조회합니다.", tags = {"MypageController"})
    @GetMapping("/manselect/{empNo}")
    public ResponseEntity<ResponseDTO> manselect(@AuthenticationPrincipal LoginMemberDTO member,
                                                 @PathVariable int empNo,
                                                 @RequestParam(name = "offset", defaultValue = "1") String offset) {

        log.info("[Controller] manselect start=================");
        log.info("[Controller] manselect offset=========== : {}", offset);

        int total = mypageService.manselectTotal(empNo);
        Criteria cri = new Criteria(Integer.valueOf(offset), 10);


        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(mypageService.selectManListWithPaging(empNo, cri));
        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));

        log.info("Controller] managementDTO selectManList pagingResponseDTO^^^^^^ {}", pagingResponseDTO);
        log.info("[Controller] manselect end=================");


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "근태 조회 성공", pagingResponseDTO));

    }

}

