package com.highright.highcare.admin.repository;

import com.highright.highcare.admin.entity.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, String> {
    List<MenuGroup> findAllByOrderByGroupCodeAsc();
}
