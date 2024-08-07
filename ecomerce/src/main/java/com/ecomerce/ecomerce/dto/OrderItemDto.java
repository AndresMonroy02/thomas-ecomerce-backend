package com.ecomerce.ecomerce.dto;


import java.math.BigDecimal;
import java.util.Set;

import com.ecomerce.ecomerce.entity.Category;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderItemDto {

    private Long productId;
    private String productName;
    private String image;
    private int quantity;
    private Set<Category> categories;
    private BigDecimal momentPrice;
    private BigDecimal pricePay;
    private double discount;

}
