package org.chendev.tasteexpress.server.service;

import org.chendev.tasteexpress.pojo.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCart add(ShoppingCart req);

    ShoppingCart sub(ShoppingCart req);

    List<ShoppingCart> list();

    void clean();
}