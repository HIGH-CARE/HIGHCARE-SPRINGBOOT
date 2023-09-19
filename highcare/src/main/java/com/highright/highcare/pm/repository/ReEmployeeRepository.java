package com.highright.highcare.pm.repository;

import com.highright.highcare.pm.entity.PmDepartment;
import com.highright.highcare.pm.entity.ReEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReEmployeeRepository extends JpaRepository<ReEmployee, Integer> {
    ReEmployee findByEmpNo(int empNo);

    List<ReEmployee> findAll();


}

