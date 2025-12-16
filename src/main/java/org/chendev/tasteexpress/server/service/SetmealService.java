package org.chendev.tasteexpress.server.service;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.SetmealDTO;
import org.chendev.tasteexpress.pojo.dto.SetmealPageQueryDTO;
import org.chendev.tasteexpress.pojo.entity.Setmeal;
import org.chendev.tasteexpress.pojo.vo.DishItemVO;
import org.chendev.tasteexpress.pojo.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    void update(SetmealDTO setmealDTO);

    void startOrStop(EnabledStatus status, Long id);

    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> getDishItemById(Long id);
}
