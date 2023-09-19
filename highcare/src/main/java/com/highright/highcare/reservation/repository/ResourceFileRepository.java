package com.highright.highcare.reservation.repository;

import com.highright.highcare.reservation.entity.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceFileRepository extends JpaRepository<ResourceFile, Integer> {


    void deleteByResourceCode(int resourceCode);

    ResourceFile findByResourceCode(int resourceCode);
}
