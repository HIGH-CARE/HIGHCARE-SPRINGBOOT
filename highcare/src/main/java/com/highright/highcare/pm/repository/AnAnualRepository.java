package com.highright.highcare.pm.repository;

import com.highright.highcare.pm.dto.PmEmployeeDTO;
import com.highright.highcare.pm.entity.AnAnual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnAnualRepository extends JpaRepository<AnAnual, Integer> {
    List<AnAnual> findByEmpNo(int empNo);
//    Page<AnAnual> findAll(Pageable paging, PmEmployeeDTO pmEmployeeDTO);

//    Page<AnAnual> findAll(Pageable pageable);
//
//    List<AnAnual> findAll(Pageable pageable);
}
