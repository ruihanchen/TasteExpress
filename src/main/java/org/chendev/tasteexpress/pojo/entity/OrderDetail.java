package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "order_detail",
        indexes = {
                @Index(name = "idx_od_order", columnList = "order_id")
        }
)
public class OrderDetail {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String name;

    private String image;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "dish_id")
    private Long dishId;


    @Column(name = "setmeal_id")
    private Long setmealId;

    @Column(name = "dish_flavor", length = 120)
    private String dishFlavor;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", precision = 10, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "modifiers_json", length = 1000)
    private String modifiersJson;
}

