package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "setmeal_dish", indexes = {
        @Index(name="idx_smd_setmeal", columnList = "setmeal_id"),
        @Index(name="idx_smd_dish", columnList = "dish_id")
})
public class SetmealDish {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="setmeal_id", nullable=false)
    private Long setmealId;

    @Column(name="dish_id", nullable=false)
    private Long dishId;

    @Column(length=64)
    private String name;             // 菜品名快照

    @Column(precision=10, scale=2)
    private BigDecimal price;        // 菜品价快照

    private Integer copies;
}
