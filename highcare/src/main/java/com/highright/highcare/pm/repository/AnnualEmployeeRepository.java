package com.highright.highcare.pm.repository;

import com.highright.highcare.pm.entity.AnnualEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnualEmployeeRepository extends JpaRepository<AnnualEmployee, Integer> {
    AnnualEmployee findByEmpNo(int empNo);
}
