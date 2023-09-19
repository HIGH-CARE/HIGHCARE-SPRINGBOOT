package com.highright.highcare.pm.repository;

import com.highright.highcare.pm.entity.AnAnual;
import com.highright.highcare.pm.entity.MgEmployee;
import com.highright.highcare.pm.entity.PmEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MgEmployeeRepository extends JpaRepository<MgEmployee, Integer> {


    Page<MgEmployee> findAll(Pageable pageable);
}
