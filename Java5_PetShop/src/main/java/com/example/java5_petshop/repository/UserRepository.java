package com.example.java5_petshop.repository;

import com.example.java5_petshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Find user by email
    boolean existsByEmail(String email);  // Check if a user exists by email
}
