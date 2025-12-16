package org.chendev.tasteexpress.server.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.dto.DishDTO;
import org.chendev.tasteexpress.pojo.dto.DishQueryDTO;
import org.chendev.tasteexpress.pojo.vo.DishVO;
import org.chendev.tasteexpress.server.service.DishService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/admin/dishes")
@RequiredArgsConstructor
public class DishAdminController {

    private final DishService dishService;

    /**
     * Create a new dish including its flavors.
     * This endpoint is used by internal operators to maintain the menu.
     */
    @PostMapping
    public Result<Long> save(@RequestBody @Valid DishDTO dto) {
        log.info("Create new dish: {}", dto.getName());
        dishService.createDish(dto);
        return Result.success();
    }

    /**
     * Paginated search for dishes in the back office.
     * Supports fuzzy name, category filter, and status filter.
     */
    @GetMapping("/page")
    public Result<PageResult<DishVO>> page(@Valid DishQueryDTO dto) {
        log.info("Dish page query: {}", dto);
        return Result.success(dishService.pageQuery(dto));
    }

    /**
     * Batch delete dishes by id.
     * Only dishes that are not on sale and not referenced by other entities
     * can be safely deleted.
     */
    @DeleteMapping
    public Result<Void> delete(@RequestParam List<Long> ids) {
        log.info("Delete dishes in batch: {}", ids);
        dishService.deleteDishes(ids);
        return Result.success();
    }

    /**
     * Fetch a single dish together with its flavor configuration.
     * Used primarily for edit forms in the admin UI.
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("Get dish by id: {}", id);
        return Result.success(dishService.getDishWithFlavors(id));
    }

    /**
     * Update dish basic information and its flavors.
     * This is a full replacement operation for the flavor list.
     */
    @PutMapping
    public Result<Void> update(@RequestBody @Valid DishDTO dto) {
        log.info("Update dish: {}", dto.getId());
        dishService.updateDish(dto);
        return Result.success();
    }

    /**
     * List dishes within a category for admin usage.
     * This is helpful when building set meals or configuring combos.
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(@RequestParam Long categoryId) {
        log.info("List dishes for admin, categoryId={}", categoryId);
        return Result.success(dishService.listByCategoryForAdmin(categoryId));
    }

    /**
     * Toggle dish on/off sale.
     * Admin operators use this to control what is visible to consumers.
     */
    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable EnabledStatus status,
                                    @RequestParam Long id) {
        log.info("Update dish status, id={}, status={}", id, status);
        dishService.changeStatus(id, status);
        return Result.success();
    }
}
