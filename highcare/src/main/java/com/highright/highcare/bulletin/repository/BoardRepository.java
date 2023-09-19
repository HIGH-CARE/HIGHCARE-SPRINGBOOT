package com.highright.highcare.bulletin.repository;

import com.highright.highcare.bulletin.entity.Board;
import com.highright.highcare.bulletin.entity.BulletinCategories;
import com.highright.highcare.bulletin.entity.BulletinEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAll();

    List<Board> findByDeleteYnAndBulletinCategories(char n, BulletinCategories byCategoryCode);
    Page<Board> findByDeleteYnAndBulletinCategories(char n, BulletinCategories byCategoryCode,Pageable paging);

    List<Board> findByDeleteYn(char n);
    Page<Board> findByDeleteYn(char n, Pageable paging);

    List<Board> findByDeleteYnAndBulletinCategoriesAndTitleContains(char n, BulletinCategories byCategoryCode, String title);
    Page<Board> findByDeleteYnAndBulletinCategoriesAndTitleContains(char n, BulletinCategories byCategoryCode, String title,Pageable paging);

    List<Board> findByDeleteYnAndTitleContains(char n, String title);
    Page<Board> findByDeleteYnAndTitleContains(char n, String title,Pageable paging);
    List<Board> findByDeleteYnAndBulletinEmployee(char n, BulletinEmployee bulletinEmployee);

    Page<Board> findByDeleteYnAndBulletinEmployee(char n, BulletinEmployee bulletinEmployee, Pageable paging);

    List<Board> findByDeleteYnAndBulletinEmployeeAndTitleContains(char n, BulletinEmployee bulletinEmployee, String content);
    Page<Board> findByDeleteYnAndBulletinEmployeeAndTitleContains(char n, BulletinEmployee bulletinEmployee, String content, Pageable paging);

    List<Board> findTop5ByDeleteYnAndBulletinCategoriesOrderByModifiedDateDesc(char n, BulletinCategories bulletinCategories);
}
