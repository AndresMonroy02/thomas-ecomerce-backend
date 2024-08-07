package com.ecomerce.ecomerce.repository;

import com.ecomerce.ecomerce.entity.Order;
import com.ecomerce.ecomerce.dto.ProductSalesDto;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);


    @Query(value = "SELECT p.id AS productId, p.name AS productName, SUM(i.quantity) AS totalQuantity, SUM(i.price_pay) AS totalSales " +
        "FROM orders o JOIN order_item i ON o.id = i.order_id JOIN product p ON p.id = i.product_id " +
        "GROUP BY p.id, p.name " +
        "ORDER BY totalSales DESC",
    nativeQuery = true)
    List<Object[]> findTopSellingProducts();

}
