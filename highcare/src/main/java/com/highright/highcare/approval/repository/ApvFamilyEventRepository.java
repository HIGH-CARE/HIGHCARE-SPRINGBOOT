package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvFamilyEvent;
import com.highright.highcare.approval.entity.ApvIssuance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ApvFamilyEventRepository extends JpaRepository<ApvFamilyEvent, Long> {

    @Modifying
    @Query("DELETE FROM ApvFamilyEvent AL WHERE AL.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);
}
