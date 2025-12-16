package org.chendev.tasteexpress.server.controller.user;

import lombok.RequiredArgsConstructor;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.vo.CategoryVO;
import org.chendev.tasteexpress.server.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user/category")
@RequiredArgsConstructor
public class CategoryUserController {

    private final CategoryService categoryService;

    /**
     * Public-facing API to list categories.
     * Only enabled categories are returned to the client.
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> list(@RequestParam(required = false) Integer type) {
        List<CategoryVO> list = categoryService.list(type, true);
        return Result.success(list);
    }
}
