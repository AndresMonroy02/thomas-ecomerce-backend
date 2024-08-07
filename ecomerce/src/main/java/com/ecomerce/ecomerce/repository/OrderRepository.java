package com.ecomerce.ecomerce.repository;

import com.ecomerce.ecomerce.entity.Order;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
