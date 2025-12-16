package org.chendev.tasteexpress.server.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.dto.CategoryDTO;
import org.chendev.tasteexpress.pojo.dto.CategoryPageQueryDTO;
import org.chendev.tasteexpress.pojo.vo.CategoryVO;
import org.chendev.tasteexpress.server.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    /**
     * Create a new category.
     * New categories start in DISABLED state so they can be configured before going live.
     */
    @PostMapping
    public Result<Void> save(@RequestBody @Valid CategoryDTO dto) {
        log.info("Create new category: {}", dto);
        categoryService.save(dto);
        return Result.success();
    }

    /**
     * Paginated category search for the admin portal.
     * Supports optional filtering by name and type.
     */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO dto) {
        log.info("Category page query: {}", dto);
        PageResult pageResult = categoryService.pageQuery(dto);
        return Result.success(pageResult);
    }

    /**
     * Delete a category by id.
     * A category cannot be deleted if it is still referenced by dishes or set-meals.
     */
    @DeleteMapping
    public Result<Void> deleteById(@RequestParam Long id) {
        log.info("Delete category id={}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * Update category metadata (name, type, sort order).
     */
    @PutMapping
    public Result<Void> update(@RequestBody @Valid CategoryDTO dto) {
        log.info("Update category id={}, name={}", dto.getId(), dto.getName());
        categoryService.update(dto);
        return Result.success();
    }

    /**
     * Enable or disable a category.
     * Disabling a category will hide it from the customer-facing menu.
     */
    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable("status")EnabledStatus status,
                                    @RequestParam Long id) {
        log.info("Change category status, id={}, status={}", id, status);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * List categories for the admin UI.
     * Admin can see all categories regardless of status.
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> list(@RequestParam(required = false) Integer type) {
        List<CategoryVO> list = categoryService.list(type, false);
        return Result.success(list);
    }
}
