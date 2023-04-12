package com.ismailcet.CafeManagement.repository;

import com.ismailcet.CafeManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmailId(@Param("email") String email);


}
