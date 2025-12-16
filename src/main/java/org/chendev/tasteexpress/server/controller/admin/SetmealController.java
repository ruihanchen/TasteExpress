package org.chendev.tasteexpress.server.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.dto.SetmealDTO;
import org.chendev.tasteexpress.pojo.dto.SetmealPageQueryDTO;
import org.chendev.tasteexpress.pojo.entity.Setmeal;
import org.chendev.tasteexpress.pojo.vo.DishItemVO;
import org.chendev.tasteexpress.pojo.vo.SetmealVO;
import org.chendev.tasteexpress.server.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@RequiredArgsConstructor
public class SetmealController {

    private final SetmealService setmealService;

    @PostMapping
    public Result<Void> save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        return Result.success(setmealService.pageQuery(setmealPageQueryDTO));
    }

    @DeleteMapping
    public Result<Void> delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        return Result.success(setmealService.getByIdWithDish(id));
    }

    @PutMapping
    public Result<Void> update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable EnabledStatus status, @RequestParam Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }


    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal) {
        return Result.success(setmealService.list(setmeal));
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> dishItems(@PathVariable Long id) {
        return Result.success(setmealService.getDishItemById(id));
    }
}
