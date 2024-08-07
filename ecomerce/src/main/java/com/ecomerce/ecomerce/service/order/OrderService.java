package com.ecomerce.ecomerce.service.order;

import com.ecomerce.ecomerce.dto.OrderDto;
import com.ecomerce.ecomerce.dto.ProductSalesDto;
import com.ecomerce.ecomerce.dto.OrderItemDto;
import com.ecomerce.ecomerce.entity.CustomUser;
import com.ecomerce.ecomerce.entity.Order;
import com.ecomerce.ecomerce.entity.OrderItem;
import com.ecomerce.ecomerce.entity.Product;
import com.ecomerce.ecomerce.enums.OrderStatus;
import com.ecomerce.ecomerce.exceptions.ResourceNotFoundException;
import com.ecomerce.ecomerce.repository.CustomUserRepository;
import com.ecomerce.ecomerce.repository.OrderRepository;
import com.ecomerce.ecomerce.repository.ProductRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final CustomUserRepository customUserRepository;
    private final ProductRepository productRepository;

    public Page<OrderDto> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToDto);
    }
    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        CustomUser user = customUserRepository.findById(orderDto.getIdUser())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        order.setUser(user);
        order.setStartDate(Instant.now());
        order.setState(OrderStatus.PENDING);

        // Calculate total amount if not provided
        if (orderDto.getTotalAmount() == null) {
            order.setTotalAmount(calculateTotalAmount(orderDto));
        } else {
            order.setTotalAmount(orderDto.getTotalAmount());
        }

        // Map OrderItemDto to OrderItem entities
        List<OrderItem> orderItems = orderDto.getItems().stream()
                .map(orderItemDto -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    Product product = productRepository.findById(orderItemDto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                    orderItem.setProduct(product);
                    orderItem.setQuantity(orderItemDto.getQuantity());
                    orderItem.setMomentPrice(BigDecimal.valueOf(product.getPrice()));
                    orderItem.setPricePay(orderItemDto.getPricePay());
                    orderItem.setDiscount(orderItemDto.getDiscount());
                    return orderItem;
                })
                .collect(Collectors.toList());
        order.setItems(orderItems);

        // Save the Order and convert to DTO
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private BigDecimal calculateTotalAmount(OrderDto orderDto) {
        return orderDto.getItems().stream()
                .map(item -> item.getPricePay().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderDto convertToDto(Order order) {
        List<OrderItemDto> orderItems = order.getItems().stream()
                .map(this::convertToOrderItemDto)
                .collect(Collectors.toList());

        return OrderDto.builder()
                .id(order.getId())
                .username(order.getUser().getUsername())
                .nameUser(order.getUser().getName())
                .idUser(Long.valueOf(order.getUser().getId()))
                .totalAmount(order.getTotalAmount())
                .startDate(order.getStartDate())
                .endDate(order.getEndDate())
                .state(order.getState().name())
                .items(orderItems)
                .build();
    }

    private OrderItemDto convertToOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .productId(Long.valueOf(orderItem.getProduct().getId()))
                .quantity(orderItem.getQuantity())
                .productName(orderItem.getProduct().getName())
                .categories(orderItem.getProduct().getCategories())
                .image(orderItem.getProduct().getImg())
                .momentPrice(orderItem.getMomentPrice())
                .pricePay(orderItem.getPricePay())
                .discount(orderItem.getDiscount())
                .build();
    }


    @Override
    public List<ProductSalesDto> findTopSellingProducts() {
        List<Object[]> results = orderRepository.findTopSellingProducts();
        return results.stream().map(result -> new ProductSalesDto(
            ((Number) result[0]).longValue(),
            (String) result[1],
            ((Number) result[2]).longValue(),
            ((Number) result[3]).doubleValue()
        )).collect(Collectors.toList());
    }
}
