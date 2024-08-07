package com.ecomerce.ecomerce.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@Data
public class OrderDto {

    private Long id;
    private Long idUser;
    private String username;
    private String nameUser;
    private BigDecimal totalAmount;
    private Instant startDate;
    private Instant endDate;
    private String state;
    private List<OrderItemDto> items;
}
