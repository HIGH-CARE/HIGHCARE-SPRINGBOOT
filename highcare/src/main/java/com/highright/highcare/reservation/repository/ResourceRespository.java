package com.highright.highcare.reservation.repository;

import com.highright.highcare.reservation.entity.Resource;
import com.highright.highcare.reservation.entity.ResourceArea;
import com.highright.highcare.reservation.entity.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRespository extends JpaRepository<Resource, Integer> {

    List<ResourceArea> findByResourceCategoryAndDeleteYn(ResourceCategory resourceCategory, char n);
}
