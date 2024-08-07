package com.ecomerce.ecomerce.controllers;

import com.ecomerce.ecomerce.dto.ApiResponse;
import com.ecomerce.ecomerce.dto.OrderDto;
import com.ecomerce.ecomerce.dto.ProductSalesDto;
import com.ecomerce.ecomerce.exceptions.ResourceNotFoundException;
import com.ecomerce.ecomerce.service.order.IOrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("")
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<OrderDto> orders = orderService.getOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
        try {
            OrderDto orderDto = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order retrieved successfully", orderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            // Log the exception if needed
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Error retrieving order", null));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderDto orderDto) {
        try {
            OrderDto createdOrderDto = orderService.createOrder(orderDto);
            return ResponseEntity.ok(new ApiResponse("Order created successfully", createdOrderDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            // Log the exception if needed
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Error creating order", null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> orderDtos = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("User orders retrieved successfully", orderDtos));
        } catch (Exception e) {
            // Log the exception if needed
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Error retrieving user orders", null));
        }
    }

    @GetMapping("/top-selling-products")
    public ResponseEntity<ApiResponse> getTopSellingProducts() {
        List<ProductSalesDto> topProducts = orderService.findTopSellingProducts();
        // topProducts.forEach(product -> {
        //     System.out.println(product.getProductName() + " " + product.get());
        // });
        return ResponseEntity.ok(new ApiResponse("Top selling products retrieved successfully", topProducts));
    }


}
