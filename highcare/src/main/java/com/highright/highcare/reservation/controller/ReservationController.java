package com.highright.highcare.reservation.controller;

import com.highright.highcare.common.Criteria;
import com.highright.highcare.common.PageDTO;
import com.highright.highcare.common.PagingResponseDTO;
import com.highright.highcare.common.ResponseDTO;
import com.highright.highcare.reservation.dto.ResourceCategoryDTO;
import com.highright.highcare.reservation.dto.ResourceDTO;
import com.highright.highcare.reservation.dto.ResourceReservationStatusDTO;
import com.highright.highcare.reservation.service.ResService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/res")
public class ReservationController {

    private final ResService resService;
    public ReservationController(ResService resService){
        this.resService = resService;
    }
    @Operation(summary = "시설 카테고리 조회", description = "시설 카테고리 조회합니다", tags = {"ReservationController"})
    @GetMapping("/res")
    public ResponseEntity<ResponseDTO> selectResCategory(){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "시설 카테고리 조회 성공", resService.selectResCategory()));
    }
    @Operation(summary = "시설 카테고리 추가", description = "시설 카테고리를 추가합니다.", tags = {"ReservationController"})
    @PostMapping("/category")
    public ResponseEntity<ResponseDTO> insertCategory(@RequestBody ResourceCategoryDTO resourceCategory){
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK.value(), "카테고리 추가 성공", resService.insertCategory(resourceCategory)));
    }

    @Operation(summary = "시설 추가", description = "시설을 추가합니다.", tags = {"ReservationController"})
    @PostMapping("/regist")
    public ResponseEntity<ResponseDTO> insertRes(@ModelAttribute ResourceDTO resourceDTO, MultipartFile image) throws IOException {
        System.out.println("resourceDTO : " + resourceDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED.value(),
                "시설추가 성공", resService.insertRes(resourceDTO, image)));
    }
    @Operation(summary = "시설 조회", description = "시설지역을 조회합니다.", tags = {"ReservationController"})
    @GetMapping("/resList")
    public ResponseEntity<ResponseDTO> selectRes(@RequestParam(name = "categoryCode") String code) {
        int categoryCode = Integer.parseInt(code);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "시설 지역 조회 성공", resService.selectRes(categoryCode)));
    }
    @Operation(summary = "시설 조회", description = "시설을 조회합니다.", tags = {"ReservationController"})
    @GetMapping("content")
    public ResponseEntity<ResponseDTO> selectContent(@RequestParam(name = "resourceCode") int resourceCode){

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "시설 조회 성공", resService.selectContent(resourceCode)));

    }
    @Operation(summary = "예약시간 조회", description = "예약시간을 조회합니다.", tags = {"ReservationController"})
    @GetMapping("dateRes")
    public ResponseEntity<ResponseDTO> selectDateRes(@RequestParam(name = "reservationDate")String reservationDate ,@RequestParam(name = "resourceCode")int resourceCode){
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "예약 시간 조회 성공", resService.selectDate(reservationDate,resourceCode)));
    }
    @Operation(summary = "시설 예약", description = "시설을 예약합니다.", tags = {"ReservationController"})
    @PostMapping("/status")
    public ResponseEntity<ResponseDTO> insertDateRes(@RequestBody ResourceReservationStatusDTO resourceReservationStatusDTO) throws Exception {
        System.out.println("reservationDate : " + resourceReservationStatusDTO.getReservationDate());
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(), "예약 성공", resService.insertResStatus(resourceReservationStatusDTO)));
    }
    @Operation(summary = "시설 수정", description = "시설을 수정합니다.", tags = {"ReservationController"})
    @PutMapping("/modRes")
    public ResponseEntity<ResponseDTO> updateRes(@ModelAttribute ResourceDTO resourceDTO, MultipartFile image) throws IOException {
                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "시설수정 성공", resService.updateRes(resourceDTO, image)));
    }

    @Operation(summary = "시설 수정", description = "시설의 정보를 수정합니다.", tags = {"ReservationController"})
    @PutMapping("/deleteRes")
    public ResponseEntity<ResponseDTO> deleteRes(@RequestBody ResourceDTO resourceDTO)  {
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "시설삭제 성공", resService.deleteRes(resourceDTO)));
    }
    @Operation(summary = "예약 현황 조회", description = "예약 현황을 조회합니다.", tags = {"ReservationController"})
    @GetMapping("/reser")
    public ResponseEntity<ResponseDTO> selectReser(@RequestParam(name = "empNo") int empNo,@RequestParam(name = "currentPage") int currentPage){
        Criteria cri = new Criteria(Integer.valueOf(currentPage), 10);
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        int total = resService.selectResTotal(empNo);
        pagingResponseDTO.setData(resService.selectStatusListWithPaging(cri,empNo));

        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "신청 현황 조회 성공", pagingResponseDTO));
    }
    @Operation(summary = "예약 현황 조회", description = "예약 현황을 조회합니다.", tags = {"ReservationController"})
    @GetMapping("/reserAll")
    public ResponseEntity<ResponseDTO> selectReserAll(@RequestParam(name = "currentPage") int currentPage){
        Criteria cri = new Criteria(Integer.valueOf(currentPage), 10);
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        int total = resService.selectAllResTotal();
        pagingResponseDTO.setData(resService.selectAllStatusListWithPaging(cri));

        pagingResponseDTO.setPageInfo(new PageDTO(cri, total));
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "신청 현황 조회 성공", pagingResponseDTO));
    }
    @Operation(summary = "예약 승인", description = "예약을 승인합니다.", tags = {"ReservationController"})
    @PutMapping("/approval")
    public ResponseEntity<ResponseDTO> updateReser(@RequestBody ResourceReservationStatusDTO resourceReservationStatusDTO)  {
        System.out.println("resourceReservationStatusDTO.getStatusCode() = " + resourceReservationStatusDTO.getStatusCode());
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "신청 승인", resService.updateResStatus(resourceReservationStatusDTO)));
    }
    @Operation(summary = "예약 거절", description = "예약 현황을 거절합니다.", tags = {"ReservationController"})
    @PutMapping("/rejected")
    public ResponseEntity<ResponseDTO> updateRejected(@RequestBody ResourceReservationStatusDTO resourceReservationStatusDTO)  {
        System.out.println("resourceReservationStatusDTO.getStatusCode() = " + resourceReservationStatusDTO.getStatusCode());
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK.value(),
                "신청 거절", resService.updateResrejected(resourceReservationStatusDTO)));
    }
}
