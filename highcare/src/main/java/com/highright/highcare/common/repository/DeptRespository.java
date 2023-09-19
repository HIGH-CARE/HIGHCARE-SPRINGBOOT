package com.highright.highcare.common.repository;

import com.highright.highcare.mypage.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRespository extends JpaRepository<Department, Integer> {
    Department findByName(String deptName);
}
