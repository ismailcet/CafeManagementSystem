package com.ismailcet.CafeManagement.repository;

import com.ismailcet.CafeManagement.entity.User;
import com.ismailcet.CafeManagement.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmailId(@Param("email") String email);

    @Query(
            value = "Select u.email FROM USER u WHERE u.role ='admin' ",
            nativeQuery = true
    )
    List<String> getAllAdmin();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    User findByEmail(String email);
}
