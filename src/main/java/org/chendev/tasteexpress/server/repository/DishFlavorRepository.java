package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.pojo.entity.DishFlavor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface DishFlavorRepository extends JpaRepository<DishFlavor, Long> {

    /**
     * Flavors bound to a dish.
     */
    List<DishFlavor> findByDishId(Long dishId);

    /**
     * Delete all flavors of a given dish.
     */
    void deleteByDishId(Long dishId);
}
