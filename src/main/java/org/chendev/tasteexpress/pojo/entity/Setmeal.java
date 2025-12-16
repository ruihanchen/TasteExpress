package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "setmeal", indexes = {
        @Index(name="idx_setmeal_category", columnList = "category_id")
})
public class Setmeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category_id", nullable=false)
    private Long categoryId;

    @Column(nullable=false, length=64, unique=true)
    private String name;

    @Column(precision=10, scale=2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private EnabledStatus status;

    @Column(length=255)
    private String description;

    @Column(length=255)
    private String image;

    @Column(name="created_at", nullable=false, updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    @Column(name="created_by")
    private Long createdBy;

    @Column(name="updated_by")
    private Long updatedBy;
}
