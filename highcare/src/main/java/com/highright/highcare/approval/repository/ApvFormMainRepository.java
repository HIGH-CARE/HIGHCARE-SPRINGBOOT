package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvFormMain;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface ApvFormMainRepository extends JpaRepository<ApvFormMain, Long> {

    List<ApvFormMain> findByEmpNoAndApvStatusOrderByApvNoDesc(@Param("empNo")int empNo, @Param("apvStatus")String apvStatus);
    Page<ApvFormMain> findByEmpNoAndApvStatusOrderByApvNoDesc(@Param("empNo")int empNo, @Param("apvStatus")String apvStatus, Pageable paging);

    // 수신함
    @Query(value = "SELECT AF.* " +
            ", AL.ISAPPROVAL " +
            ", E.EMP_NAME " +
            ", J.JOB_NAME AS jobName " +
            "FROM TBL_APV_FORM AF " +
            "JOIN TBL_APV_LINE AL ON AL.APV_NO = AF.APV_NO " +
            "JOIN TBL_EMPLOYEE E ON AF.EMP_NO = E.EMP_NO " +
            "JOIN TBL_JOB J ON E.JOB_CODE = J.JOB_CODE " +
            "WHERE AL.EMP_NO = :empNo " +
            "AND AL.ISAPPROVAL = :isApproval " +
            "AND AL.DEGREE <> 0 " +
            "ORDER BY AF.APV_NO DESC " , nativeQuery = true)
    List<ApvFormMain> findByEmpNoAndApvStatus2(@Param("empNo") int empNo, @Param("isApproval")String isApproval);

    @Query(value = "SELECT AF.* " +
            ", AL.ISAPPROVAL " +
            ", E.EMP_NAME " +
            ", J.JOB_NAME AS jobName " +
            "FROM TBL_APV_FORM AF " +
            "JOIN TBL_APV_LINE AL ON AL.APV_NO = AF.APV_NO " +
            "JOIN TBL_EMPLOYEE E ON AF.EMP_NO = E.EMP_NO " +
            "JOIN TBL_JOB J ON E.JOB_CODE = J.JOB_CODE " +
            "WHERE AL.EMP_NO = :empNo " +
            "AND AF.APV_STATUS = :apvStatus " +
            "AND AL.DEGREE <> 0 " +
            "ORDER BY AF.APV_NO DESC " , nativeQuery = true)
    List<ApvFormMain> findByEmpNoAndApvStatus3(@Param("empNo") int empNo, @Param("apvStatus")String apvStatus);

    // 메인페이지 리스트
//    @Query(value = "SELECT A.TITLE " +
//            "FROM TBL_APV_FORM A " +
//            "WHERE A.EMP_NO = :empNo " +
//            "AND ROWNUM <=10 " +
//            "ORDER BY A.WRITE_DATE DESC ", nativeQuery = true)
//    List<ApvFormMain> findTitlesByEmpNo(@Param("empNo")int empNo);

    @Query(value = "SELECT a.title " +
            "FROM ApvForm a " +
            "WHERE a.empNo = :empNo " +
            "ORDER BY a.writeDate DESC ", nativeQuery = true)
    List<String> findTitlesByEmpNoOrderByWriteDateDesc(@Param("empNo") int empNo);

    // 1. 오늘 - 결재진행중
    @Query(value = "SELECT COUNT(*) " +
            "FROM TBL_APV_FORM AF " +
            "WHERE AF.EMP_NO =:empNo " +
            "AND AF.APV_STATUS = '결재진행중' " +
            "AND TRUNC(AF.WRITE_DATE) = TRUNC(SYSDATE) ", nativeQuery = true)
    int countByEmpNoAndApvStatus1(@Param("empNo")int empNo);

    // 1. 오늘 - 긴급
    @Query(value = "SELECT COUNT(*) " +
            "FROM TBL_APV_FORM AF " +
            "WHERE AF.EMP_NO =:empNo " +
            "AND AF.APV_STATUS = '결재진행중' " +
            "AND AF.ISURGENCY = 'T' " +
            "AND TRUNC(AF.WRITE_DATE) = TRUNC(SYSDATE) ", nativeQuery = true)
    int countByEmpNoAndIsUrgencyToday(@Param("empNo")int empNo);

    // 2. 결재진행중, 결재반려
    int countByEmpNoAndApvStatus(@Param("empNo")int empNo, @Param("apvStatus")String apvStatus);


    // 3. 긴급수신
    int countByEmpNoAndIsUrgency(@Param("empNo")int empNo, @Param("isUrgency")String T);

    // 4. 신규수신
@Query(value = "SELECT COUNT(*) " +
        "FROM TBL_APV_LINE AL " +
        "JOIN TBL_EMPLOYEE E ON AL.EMP_NO = E.EMP_NO " +
        "WHERE AL.EMP_NO = :empNo " +
        "AND AL.ISAPPROVAL =:isApproval " , nativeQuery = true)
    int countByEmpNoAndIsApprovalReceive(@Param("empNo")int empNo, @Param("isApproval")String isApproval);

    // 출장신청서 조회
//    @Query(value = "SELECT BT.* FROM TBL_APV_BUSINESS_TRIP BT " +
//            "JOIN TBL_APV_FORM AF ON AF.APV_NO = BT.APV_NO " +
//            "WHERE AF.EMP_NO = :empNo " +
//            "AND AF.TITLE='출장신청서' ", nativeQuery = true)
    ApvFormMain findByEmpNo(@Param("empNo") int empNo);
}
