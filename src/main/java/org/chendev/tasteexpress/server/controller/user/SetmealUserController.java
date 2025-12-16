package org.chendev.tasteexpress.server.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.entity.Setmeal;
import org.chendev.tasteexpress.pojo.vo.DishItemVO;
import org.chendev.tasteexpress.server.service.SetmealService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/setmeal")
@RequiredArgsConstructor
public class SetmealUserController {

    private final SetmealService setmealService;

    // GET /user/setmeal/list?categoryId=xxx
    @GetMapping("/list")
    public Result<List<Setmeal>> list(@RequestParam Long categoryId) {
        Setmeal filter = Setmeal.builder()
                .categoryId(categoryId)
                .status(EnabledStatus.ENABLED)
                .build();
        return Result.success(setmealService.list(filter));
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dishItems(@PathVariable Long id) {
        return Result.success(setmealService.getDishItemById(id));
    }
}
