package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvMeetingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ApvMeetingLogRepository extends JpaRepository<ApvMeetingLog, Long> {

    @Modifying
    @Query("DELETE FROM ApvMeetingLog AL WHERE AL.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);


}
