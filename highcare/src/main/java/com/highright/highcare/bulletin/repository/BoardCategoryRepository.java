package com.highright.highcare.bulletin.repository;

import com.highright.highcare.bulletin.entity.Board;
import com.highright.highcare.bulletin.entity.BulletinCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCategoryRepository extends JpaRepository<BulletinCategories, Integer> {
    BulletinCategories findByNameBoard(String nameBoard);

    BulletinCategories findByCategoryCode(int categoryCode);
    List<BulletinCategories> findAll();
}
