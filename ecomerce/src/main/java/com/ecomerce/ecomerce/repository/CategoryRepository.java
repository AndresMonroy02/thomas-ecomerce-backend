package com.ecomerce.ecomerce.repository;

import com.ecomerce.ecomerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository; 

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    boolean existsByName(String name);
    Page<Category> getCategoriesByNameContaining(String name, Pageable pageable);
  }
  