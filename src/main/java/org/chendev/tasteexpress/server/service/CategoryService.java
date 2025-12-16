package org.chendev.tasteexpress.server.service;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.CategoryDTO;
import org.chendev.tasteexpress.pojo.dto.CategoryPageQueryDTO;
import org.chendev.tasteexpress.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    void save(CategoryDTO dto);

    PageResult pageQuery(CategoryPageQueryDTO dto);

    void deleteById(Long id);

    void update(CategoryDTO dto);

    void startOrStop(EnabledStatus status, Long id);

    /**
     * List categories, optionally filtering by type and enabled status.
     *
     * @param type        optional category type; null = all types
     * @param onlyEnabled if true, only return categories with status=ENABLE
     */
    List<CategoryVO> list(Integer type, boolean onlyEnabled);
}