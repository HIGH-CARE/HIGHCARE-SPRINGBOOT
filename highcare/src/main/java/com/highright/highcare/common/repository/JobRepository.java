package com.highright.highcare.common.repository;

import com.highright.highcare.mypage.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {

    Job findByName(String jobName);
}
