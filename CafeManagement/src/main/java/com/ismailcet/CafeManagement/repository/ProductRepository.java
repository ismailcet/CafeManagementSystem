package com.ismailcet.CafeManagement.repository;

import com.ismailcet.CafeManagement.entity.Product;
import com.ismailcet.CafeManagement.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p set p.status = ?1 where p.id = ?2 ")
    Integer updateProductStatus(String status,Integer id);

    List<Product> findByCategoryId(Integer id);
}
