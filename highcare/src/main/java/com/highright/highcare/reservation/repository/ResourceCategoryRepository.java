package com.highright.highcare.reservation.repository;

import com.highright.highcare.reservation.entity.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Integer> {
}
