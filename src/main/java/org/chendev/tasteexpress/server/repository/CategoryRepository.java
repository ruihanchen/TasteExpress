package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.pojo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    @Query("""
           select c from Category c
           where (:name is null or lower(c.name) like lower(concat('%', :name, '%')))
             and (:type is null or c.type = :type)
           """)
    Page<Category> pageQuery(@Param("name") String name,
                             @Param("type") Integer type,
                             Pageable pageable);

    List<Category> findByTypeOrderBySortAsc(Integer type);

    List<Category> findByTypeAndStatusOrderBySortAsc(Integer type, EnabledStatus status);

    List<Category> findAllByOrderBySortAsc();

    List<Category> findAllByStatusOrderBySortAsc(EnabledStatus status);
}
