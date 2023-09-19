package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvIssuance;
import com.highright.highcare.approval.entity.ApvVacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ApvIssuanceRepository extends JpaRepository<ApvIssuance, Long> {

    @Modifying
    @Query("DELETE FROM ApvIssuance AL WHERE AL.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);
}
