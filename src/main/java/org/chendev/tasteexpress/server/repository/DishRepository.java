package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.pojo.entity.Dish;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Dish entities.
 *
 * Design goals:
 * - Consistent naming and case-insensitive lookups
 * - Explicit @Param usage for readability and robustness
 * - JPQL queries that are easy to maintain
 */
public interface DishRepository extends JpaRepository<Dish, Long> {

    // ---------- Existence / lookup ----------

    /**
     * Check if a dish name exists (case-insensitive).
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Lookup by name (case-insensitive).
     */
//    Optional<Object> findByName(String name);
    Optional<Dish> findByNameIgnoreCase(String name);

    // ---------- Paging / filtering ----------

    /**
     * Flexible paged query with optional filters.
     *
     * We keep case-insensitive search on name to match the behavior of
     * findByNameIgnoreCase and avoid subtle differences.
     */
    @Query("""
           select d
             from Dish d
            where (:name is null or lower(d.name) like lower(concat('%', :name, '%')))
              and (:categoryId is null or d.categoryId = :categoryId)
              and (:status is null or d.status = :status)
           """)
    Page<Dish> pageQuery(@Param("name") String name,
                                 @Param("categoryId") Long categoryId,
                                 @Param("status") EnabledStatus status,
                                 Pageable pageable);

    // ---------- Category-based reads ----------

    /**
     * Dishes by category + status, sorted by updatedAt desc for admin dashboard.
     */
    List<Dish> findByCategoryIdAndStatusOrderByUpdatedAtDesc(Long categoryId, EnabledStatus status);

    /**
     * Dishes by category + status (no specific sort).
     * Useful for simple user-facing lists.
     */
    List<Dish> findByCategoryIdAndStatus(Long categoryId, EnabledStatus status);

    /**
     * Count how many dishes are assigned to a category.
     * Typically used when validating category deletion.
     */
    long countByCategoryId(Long categoryId);

    // ---------- Setmeal relation ----------

    /**
     * All dishes that belong to a given setmeal.
     * distinct is used to avoid duplicates due to joins.
     */
    @Query("""
           select distinct d
             from Dish d
             join SetmealDish sd on sd.dishId = d.id
            where sd.setmealId = :setmealId
           """)
    List<Dish> findBySetmealId(@Param("setmealId") Long setmealId);

}
