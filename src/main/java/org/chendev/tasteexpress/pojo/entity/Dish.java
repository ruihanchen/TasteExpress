package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "dish",
        indexes = @Index(name = "idx_dish_category", columnList = "category_id")
)
@EntityListeners(AuditingEntityListener.class)
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32, unique = true)
    private String name;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 255)
    private String image;

    @Column(length = 255)
    private String description;

    /**
     * 1=on sale, 0=off sale
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private EnabledStatus status;;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;
}
