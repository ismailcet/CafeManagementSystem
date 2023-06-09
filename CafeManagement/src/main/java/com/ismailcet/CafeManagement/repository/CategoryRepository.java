package com.ismailcet.CafeManagement.repository;

import com.ismailcet.CafeManagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(
            value = "SELECT c FROM Category c WHERE c.id in (SELECT p.category from Product p WHERE p.status='true' )"
    )
    List<Category> getAllCategory();
}
