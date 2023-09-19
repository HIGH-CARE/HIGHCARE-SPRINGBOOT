package com.highright.highcare.pm.repository;

import com.highright.highcare.pm.entity.Management;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ManagementEmRepository extends JpaRepository<Management, Integer> {

    /* 출 퇴근 조회 */
    Page<Management> findByManNo(String manNo, Pageable paging);

    List<Management> findAll();

    Optional<Management> findByManDateAndEmpNo(String manDate, Integer empNo);

    /* 퇴근 조회용 */
    Page<Management> findByEmpNo(int empNo, Pageable paging);
    Management findByEmpNo(int empNo);


    Page<Management> findByEmpNo(int empNo, String manDate, Pageable paging);
    Management findByEmpNoAndEndTimeIsNull(Integer empNo);
}
