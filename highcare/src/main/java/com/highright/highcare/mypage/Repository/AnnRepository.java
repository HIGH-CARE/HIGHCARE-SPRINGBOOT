package com.highright.highcare.mypage.Repository;

import com.highright.highcare.mypage.entity.MyAnnual;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface AnnRepository extends JpaRepository<MyAnnual, Integer> {

    @Query(value = "SELECT * FROM TBL_ANNUAL A " +
            "JOIN TBL_APV_VACATION B ON (A.APV_NO = B.APV_NO) " +
            "WHERE A.EMP_NO = :empNo " +
            "ORDER BY A.ANN_NO DESC ",
            nativeQuery = true)
    List<MyAnnual> findByEmpNo(int empNo);


    Page<MyAnnual> findByEmpNo(@Param("empNo") int empNo, Pageable paging);

}
