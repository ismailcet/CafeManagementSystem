package com.ismailcet.CafeManagement.repository;

import com.ismailcet.CafeManagement.entity.Product;
import com.ismailcet.CafeManagement.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

}
