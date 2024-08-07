package com.ecomerce.ecomerce.repository;

import com.ecomerce.ecomerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT DISTINCT p FROM Product p " +
    "LEFT JOIN p.categories c " +
    "WHERE p.price BETWEEN :minPrice AND :maxPrice " +
    "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
    "AND (LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
    "AND (c.name IN :categories) " +
    "ORDER BY p.id ASC")
        Page<Product> findCustomFiltered(
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("name") String name,
        @Param("description") String description,
        @Param("categories") List<String> categories,
        Pageable pageable
    );
}
