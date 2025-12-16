package org.chendev.tasteexpress.server.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.entity.ShoppingCart;
import org.chendev.tasteexpress.server.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart req) {
        return Result.success(shoppingCartService.add(req));
    }

    @PostMapping("/sub")
    public Result<ShoppingCart> sub(@RequestBody ShoppingCart req) {
        return Result.success(shoppingCartService.sub(req));
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        return Result.success(shoppingCartService.list());
    }

    @DeleteMapping("/clean")
    public Result<Void> clean() {
        shoppingCartService.clean();
        return Result.success();
    }
}
