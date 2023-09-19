package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvLine;
import com.highright.highcare.approval.entity.ApvMeetingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;


public interface ApvLineRepository extends JpaRepository<ApvLine, Long> {


    @Modifying
    @Query(value = "UPDATE TBL_APV_LINE AL " +
            "SET AL.ISAPPROVAL = 'T', " +
            "    AL.APV_DATE = SYSDATE " +
            "WHERE AL.APV_LINE_NO = :apvLineNo ", nativeQuery = true)
    void updateIsApproval(@Param("apvLineNo") Long apvLineNo);


    @Query(value = "SELECT COUNT(*) FROM TBL_APV_LINE " +
            "WHERE ISAPPROVAL = 'F' " +
            "AND APV_NO = (SELECT APV_NO FROM TBL_APV_LINE " +
            "WHERE APV_LINE_NO = :apvLineNo)", nativeQuery = true)
    int areAllApproved(@Param("apvLineNo") Long apvLineNo);


    @Query(value = "SELECT COUNT(*) FROM TBL_APV_LINE " +
            "WHERE ISAPPROVAL = 'F' " +
            "AND APV_NO = :apvNo ", nativeQuery = true)
    int apvNoAllApproved(@Param("apvNo") Long apvNo);

    @Modifying
    @Query("DELETE FROM ApvLine AL WHERE AL.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);
}
