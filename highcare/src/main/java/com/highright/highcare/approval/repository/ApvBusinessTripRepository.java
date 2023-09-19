package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvBusinessTrip;
import com.highright.highcare.approval.entity.ApvForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ApvBusinessTripRepository extends JpaRepository<ApvBusinessTrip, Long> {

    // 출장신청서 조회
    @Query(value = "SELECT BT.ITEMS_NO, BT.BT_PURPOSE, BT.START_DATE, BT.START_TIME, " +
            "BT.END_DATE, BT.END_TIME, BT.BT_LOCATION, BT.TRIP_ATTENDEES, BT.APV_NO " +
            "FROM TBL_APV_BUSINESS_TRIP BT " +
            "JOIN TBL_APV_FORM AF ON AF.APV_NO = BT.APV_NO " +
            "WHERE AF.EMP_NO = :empNo ", nativeQuery = true)
    List<ApvBusinessTrip> findByEmpNo(@Param("empNo") int empNo);

    @Modifying
    @Query("DELETE FROM ApvBusinessTrip AL WHERE AL.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);

    List<ApvBusinessTrip> findByApvNo(@Param("apvNo")Long refApvNo);
}
