package org.chendev.tasteexpress.server.service;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.*;
import org.chendev.tasteexpress.pojo.vo.*;


import java.util.List;

public interface DishService {

    void createDish(DishDTO dto);

    void updateDish(DishDTO dto);

    void deleteDishes(List<Long> ids);

    void changeStatus(Long id, EnabledStatus status);

    PageResult<DishVO> pageQuery(DishQueryDTO dto);

    DishVO getDishWithFlavors(Long id);

    /**
     * Admin: list dishes by category (no flavor detail needed).
     */
    List<DishVO> listByCategoryForAdmin(Long categoryId);

    /**
     * User: list available dishes (with flavors) for a category.
     */
    List<DishVO> listByCategoryForUser(Long categoryId);
}
