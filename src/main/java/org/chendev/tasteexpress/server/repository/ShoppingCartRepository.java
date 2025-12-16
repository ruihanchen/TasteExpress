package org.chendev.tasteexpress.server.repository;

import org.chendev.tasteexpress.pojo.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    List<ShoppingCart> findByUserIdOrderByCreatedAtAsc(Long userId);


    ShoppingCart findByUserIdAndDishIdAndDishFlavor(Long userId, Long dishId, String dishFlavor);


    ShoppingCart findByUserIdAndSetmealId(Long userId, Long setmealId);

    long deleteByUserId(Long userId);
}
