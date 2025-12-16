package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.pojo.entity.SetmealDish;
import org.chendev.tasteexpress.pojo.vo.DishItemVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetmealDishRepository extends JpaRepository<SetmealDish, Long> {

    List<SetmealDish> findBySetmealId(Long setmealId);

    void deleteBySetmealId(Long setmealId);

    @Query("""
    select new org.chendev.tasteexpress.pojo.vo.DishItemVO(
        d.name, d.image, d.description, d.price, sd.copies
    )
    from SetmealDish sd
    join Dish d on sd.dishId = d.id
    where sd.setmealId = :setmealId
""")
    List<DishItemVO> findDishItemsBySetmealId(@Param("setmealId") Long setmealId);
}
