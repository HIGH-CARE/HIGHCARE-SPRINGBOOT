package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvForm;
import com.highright.highcare.approval.entity.ApvFormMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ApvFormRepository extends JpaRepository<ApvForm, Long> {

    ApvForm findByApvNo(Long apvNo);

    @Query(value = "SELECT DISTINCT A.TITLE " +
            "FROM TBL_APV_FORM A " +
            "ORDER BY A.WRITE_DATE DESC " +
            "FETCH FIRST 5 ROWS ONLY ", nativeQuery = true)
    List<String> findTitlesByEmpNo(@Param("empNo")int empNo);

    @Modifying
    @Query("UPDATE ApvForm af SET af.apvStatus = '결재완료' WHERE af.apvNo = :apvNo")
    void updateApvStatusToCompleted(@Param("apvNo") Long apvNo);

    @Modifying
    @Query("UPDATE ApvForm af SET af.apvStatus = '결재진행중' WHERE af.apvNo = :apvNo")
    void updateApvStatusToProcess(@Param("apvNo") Long apvNo);

    @Modifying
    @Query("UPDATE ApvForm af SET af.apvStatus = '결재반려' WHERE af.apvNo = :apvNo")
    void updateApvStatusToReject(@Param("apvNo") Long apvNo);

    @Query(value = "SELECT a.title " +
            "FROM ApvForm a " +
            "WHERE a.empNo = :empNo " +
            "ORDER BY a.writeDate DESC ", nativeQuery = true)
    List<String> findTitlesByEmpNoOrderByWriteDateDesc(@Param("empNo") int empNo);


    @Modifying
    @Query(value = "UPDATE TBL_APV_LINE AL " +
            "SET AL.ISAPPROVAL = 'F' " +
            "WHERE AL.APV_NO = :apvNo " +
            "AND AL.ISAPPROVAL <> 'F'", nativeQuery = true)
    void updateIsApprovalToFalse(@Param("apvNo") Long apvNo);

    @Query("SELECT af.title FROM ApvForm af WHERE af.apvNo = :apvNo")
    String findTitleByApvNo(@Param("apvNo") Long apvNo);

}