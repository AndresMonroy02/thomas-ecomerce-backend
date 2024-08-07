package com.ecomerce.ecomerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecomerce.ecomerce.entity.CustomUser;

import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Integer> {
    
    Optional<CustomUser> findByEmail(String email);
}
