package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "shopping_cart", indexes = {
        @Index(name = "idx_cart_user_time", columnList = "user_id, created_at")
})
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "dish_id")
    private Long dishId;

    @Column(name = "setmeal_id")
    private Long setmealId;

    @Column(name = "dish_flavor")
    private String dishFlavor;

    @Column(nullable = false)
    private Integer number = 1;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
