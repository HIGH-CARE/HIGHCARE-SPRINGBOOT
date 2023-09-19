package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvBusinessTrip;
import com.highright.highcare.approval.entity.ApvExpForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ApvExpFormRepository extends JpaRepository<ApvExpForm, Long> {

    @Modifying
    @Query("DELETE FROM ApvExpForm AL WHERE AL.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);
}
