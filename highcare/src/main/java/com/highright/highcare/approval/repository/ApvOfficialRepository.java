package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvBusinessTrip;
import com.highright.highcare.approval.entity.ApvOfficial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ApvOfficialRepository extends JpaRepository<ApvOfficial, Long> {

    @Query(value = "SELECT AO.ITEMS_NO, AO.OFFICIAL_TITLE, AO.RECEPTION, AO.APV_NO " +
            "FROM TBL_APV_OFFICIAL AO " +
            "JOIN TBL_APV_FORM AF ON AF.APV_NO = AO.APV_NO " +
            "WHERE AF.EMP_NO = :empNo ", nativeQuery = true)
    List<ApvOfficial> findByEmpNo(@Param("empNo") int empNo);

    @Modifying
    @Query("DELETE FROM ApvOfficial AO WHERE AO.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);
}
