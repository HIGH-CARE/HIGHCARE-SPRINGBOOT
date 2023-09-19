package com.highright.highcare.mypage.Repository;

import com.highright.highcare.mypage.entity.MyManegement;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyManagementRepository extends JpaRepository <MyManegement, Integer> {
    List<MyManegement> findByEmpNo(int empNo);

    Page<MyManegement> findByEmpNo(@Param("empNo") int empNo, Pageable paging);

}
