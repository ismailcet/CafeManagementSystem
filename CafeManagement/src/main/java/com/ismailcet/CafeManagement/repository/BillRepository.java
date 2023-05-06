package com.ismailcet.CafeManagement.repository;

import com.ismailcet.CafeManagement.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("SELECT b FROM Bill b ORDER BY b.id DESC")
    List<Bill> getAllBills();

    @Query("SELECT b FROM Bill b WHERE b.createdBy = ?1 ORDER BY b.id desc")
    List<Bill> getBillByUserName(String username);
}
