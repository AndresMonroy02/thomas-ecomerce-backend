package com.ecomerce.ecomerce.service.order;

import com.ecomerce.ecomerce.dto.OrderDto;
// import com.ecomerce.ecomerce.entity.Order;
import com.ecomerce.ecomerce.dto.ProductSalesDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto createOrder(OrderDto orderDto);
    Page<OrderDto> getOrders(Pageable pageable);
    List<ProductSalesDto> findTopSellingProducts();

}
