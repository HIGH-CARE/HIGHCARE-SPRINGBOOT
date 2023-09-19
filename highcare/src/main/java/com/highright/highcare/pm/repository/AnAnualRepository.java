package com.highright.highcare.pm.repository;

import com.highright.highcare.pm.dto.PmEmployeeDTO;
import com.highright.highcare.pm.entity.AnAnual;
import com.highright.highcare.pm.entity.AnEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnAnualRepository extends JpaRepository<AnAnual, Integer> {
    List<AnAnual> findByEmpNo(int empNo);


}
