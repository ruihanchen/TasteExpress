package org.chendev.tasteexpress.pojo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "category",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_category_name", columnNames = "name")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Explicitly map to "id" column to avoid ambiguity
    private Long id;

    /**
     * Category type:
     *  1 = dish category
     *  2 = set-meal category
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    /**
     * Sort order in ascending direction.
     * Lower value means higher priority in the UI.
     */
    @Column(name = "sort", nullable = false)
    private Integer sort;

    /**
     * Status:
     *  1 = enabled (visible / active)
     *  0 = disabled (hidden, not available for new items)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private EnabledStatus status;;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;
}
