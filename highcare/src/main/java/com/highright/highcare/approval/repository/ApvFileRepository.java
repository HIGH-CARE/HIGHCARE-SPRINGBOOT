package com.highright.highcare.approval.repository;

import com.highright.highcare.approval.entity.ApvBusinessTrip;
import com.highright.highcare.approval.entity.ApvFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ApvFileRepository extends JpaRepository<ApvFile, Long> {

    @Modifying
    @Query("DELETE FROM ApvFile AL WHERE AL.apvNo = :apvNo ")
    void deleteByApvNo(@Param("apvNo") Long apvNo);

    ApvFile findByOriginalFileName(@Param("fileName") String originalFileName);
}
