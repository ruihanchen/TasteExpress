package org.chendev.tasteexpress.server.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.vo.DishVO;
import org.chendev.tasteexpress.server.service.DishService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/user/dishes")
@RequiredArgsConstructor
public class DishUserController {

    private final DishService dishService;

    /**
     * List dishes for the consumer app by category.
     * The result includes flavor options so the client can build
     * a rich ordering experience.
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(@RequestParam Long categoryId) {
        log.info("User menu listing, categoryId={}", categoryId);
        List<DishVO> list = dishService.listByCategoryForUser(categoryId);
        return Result.success(list);
    }
}
