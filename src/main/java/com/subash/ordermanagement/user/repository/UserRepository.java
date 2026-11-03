package com.subash.ordermanagement.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subash.ordermanagement.user.entity.User;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
