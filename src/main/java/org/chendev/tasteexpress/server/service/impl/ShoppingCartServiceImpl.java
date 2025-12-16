package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.context.BaseContext;
import org.chendev.tasteexpress.pojo.entity.Dish;
import org.chendev.tasteexpress.pojo.entity.Setmeal;
import org.chendev.tasteexpress.pojo.entity.ShoppingCart;
import org.chendev.tasteexpress.server.repository.DishRepository;
import org.chendev.tasteexpress.server.repository.SetmealRepository;
import org.chendev.tasteexpress.server.repository.ShoppingCartRepository;
import org.chendev.tasteexpress.server.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepo;
    private final DishRepository dishRepo;
    private final SetmealRepository setmealRepo;


    private Long currentUserId() {
        return BaseContext.getCurrentId();
    }

    private boolean hasBoth(ShoppingCart req) {
        return req.getDishId() != null && req.getSetmealId() != null;
    }
    private boolean hasNeither(ShoppingCart req) {
        return req.getDishId() == null && req.getSetmealId() == null;
    }

    @Override
    @Transactional
    public ShoppingCart add(ShoppingCart req) {
        final Long uid = currentUserId();
        assertExactlyOneOfDishOrSetmeal(req);
        // Resolve existing cart line for this user and item (dish or set meal)
        ShoppingCart row = (req.getDishId() != null)
                ? findDishLine(uid, req.getDishId(), req.getDishFlavor())
                : findSetmealLine(uid, req.getSetmealId());

        if (row == null) {
            // Create a new cart line if it does not exist yet
            row = (req.getDishId() != null)
                    ? newDishLine(uid, req.getDishId(), req.getDishFlavor())
                    : newSetmealLine(uid, req.getSetmealId());
            row.setNumber(1);
            row.setCreatedAt(LocalDateTime.now());
        } else {
            row.setNumber(row.getNumber() + 1);
        }
        return cartRepo.save(row);
    }

    @Override
    @Transactional
    public ShoppingCart sub(ShoppingCart req) {
        final Long userId = currentUserId();
        assertExactlyOneOfDishOrSetmeal(req);

        ShoppingCart row = (req.getDishId() != null)
                ? findDishLine(userId, req.getDishId(), req.getDishFlavor())
                : findSetmealLine(userId, req.getSetmealId());

        if (row == null) {
            // Nothing to subtract: the item is not present in the user's cart
            log.debug("Attempted to subtract item not in cart: userId={}, req={}", userId, req);
            return null;
        }

        int qty = (row.getNumber() == null ? 0 : row.getNumber()) - 1;
        if (qty <= 0) {
            cartRepo.deleteById(row.getId());
            log.debug("Removed cart line because quantity reached zero: userId={}, cartLineId={}",
                    userId, row.getId());
            return null;
        }
        row.setNumber(qty);
        return cartRepo.save(row);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCart> list() {
        Long uid = currentUserId();
        if (uid == null) {
            throw new IllegalStateException("current user not set");
        }
        return cartRepo.findByUserIdOrderByCreatedAtAsc(uid);
    }

    @Override
    @Transactional
    public void clean() {
        Long uid = currentUserId();
        if (uid == null) {
            throw new IllegalStateException("current user not set");
        }
        long affected = cartRepo.deleteByUserId(uid);
        log.info("ShoppingCart clean userId={}, deleted={}", uid, affected);
    }




    /* ======================= Private method (used only to avoid duplication and improve clarity) ======================= */


    /**
     * Validates that exactly one of dishId or setmealId is present.
     *
     * <p>This enforces the invariant that a cart line represents either a dish
     * or a set meal, but never both.</p>
     *
     * @throws IllegalArgumentException if neither or both ids are provided
     */


    private void assertExactlyOneOfDishOrSetmeal(ShoppingCart req) {
        boolean hasDish = req.getDishId() != null;
        boolean hasSetmeal = req.getSetmealId() != null;
        if (hasDish == hasSetmeal) {
            throw new IllegalArgumentException("please provide either dishId or setmealId (exclusively)");
        }
    }

    /**
     * Looks up an existing cart line for a dish with a specific flavor.
     */

    private ShoppingCart findDishLine(Long userId, Long dishId, String flavor) {
        return cartRepo.findByUserIdAndDishIdAndDishFlavor(userId, dishId, flavor);
    }

    /**
     * Looks up an existing cart line for a set meal.
     */

    private ShoppingCart findSetmealLine(Long userId, Long setmealId) {
        return cartRepo.findByUserIdAndSetmealId(userId, setmealId);
    }

    /**
     * Creates a new cart line for a dish.
     *
     * @throws IllegalArgumentException if the dish does not exist
     */

    private ShoppingCart newDishLine(Long userId, Long dishId, String flavor) {
        Dish dish = dishRepo.findById(dishId).orElse(null);
        if (dish == null) {
            throw new IllegalArgumentException("The requested dish does not exist");
        }
        ShoppingCart row = new ShoppingCart();
        row.setUserId(userId);
        row.setDishId(dishId);
        row.setDishFlavor(flavor);
        row.setName(dish.getName());
        row.setImage(dish.getImage());
        row.setAmount(dish.getPrice());
        return row;
    }

    /**
     * Creates a new cart line for a set meal.
     *
     * @throws IllegalArgumentException if the set meal does not exist
     */

    private ShoppingCart newSetmealLine(Long userId, Long setmealId) {
        Setmeal sm = setmealRepo.findById(setmealId).orElse(null);
        if (sm == null) {
            throw new IllegalArgumentException("The requested meal does not exist");
        }
        ShoppingCart row = new ShoppingCart();
        row.setUserId(userId);
        row.setSetmealId(setmealId);
        row.setName(sm.getName());
        row.setImage(sm.getImage());
        row.setAmount(sm.getPrice());
        return row;
    }
}
