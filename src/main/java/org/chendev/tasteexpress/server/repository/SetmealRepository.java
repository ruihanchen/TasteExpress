package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.pojo.entity.Setmeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetmealRepository extends JpaRepository<Setmeal, Long> {

    @Query("""
           select s from Setmeal s
           where (:name is null or lower(s.name) like lower(concat('%', :name, '%')))
             and (:categoryId is null or s.categoryId = :categoryId)
             and (:status is null or s.status = :status)
           """)
    Page<Setmeal> pageQuery(@Param("name") String name,
                            @Param("categoryId") Long categoryId,
                            @Param("status") EnabledStatus status,
                            Pageable pageable);

    List<Setmeal> findByCategoryIdAndStatus(Long categoryId, EnabledStatus status);

    long countByCategoryId(Long categoryId);

    List<Setmeal> findByCategoryId(Long categoryId);

    List<Setmeal> findByStatus(EnabledStatus status);
}
