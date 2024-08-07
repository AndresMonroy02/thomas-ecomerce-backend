package com.ecomerce.ecomerce.entity;

import com.ecomerce.ecomerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;



@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private CustomUser user;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant endDate;

    private BigDecimal totalAmount;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus state;

}
