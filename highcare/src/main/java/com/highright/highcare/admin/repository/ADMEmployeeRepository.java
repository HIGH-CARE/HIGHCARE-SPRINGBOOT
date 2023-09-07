package com.highright.highcare.admin.repository;

import com.highright.highcare.admin.entity.ADMEmployee;
import com.highright.highcare.auth.entity.AUTHEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ADMEmployeeRepository extends JpaRepository<ADMEmployee, Integer> {
    ADMEmployee findByEmpNo(int empNo);

//    Optional<AUTHAccount> findByEmployee_EmailAndName(String address, String name);

//    @Query("SELECT e FROM ADMEmployee e WHERE e.empNo NOT IN (SELECT a.empNo FROM Account a)")
//    List<ADMEmployee> findADMEmployeesWithoutAccount();

}
