package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "dish_flavor",
        indexes = @Index(name = "idx_flavor_dish", columnList = "dish_id")
)
@EntityListeners(AuditingEntityListener.class)
public class DishFlavor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(nullable = false, length = 32)
    private String name;

    /**
     * JSON string for options, e.g.
     *  [{"label":"Mild","value":"mild"},{"label":"Spicy","value":"spicy"}]
     */
    @Column(length = 255)
    private String value;

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
