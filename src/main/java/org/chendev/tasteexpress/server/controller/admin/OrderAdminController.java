package org.chendev.tasteexpress.server.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.dto.OrderPageQueryDTO;
import org.chendev.tasteexpress.pojo.vo.OrderVO;
import org.chendev.tasteexpress.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderService orderService;

    /**
     * Back-office conditional search with fuzzy filters on number/phone and optional status/time window.
     */
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrderPageQueryDTO dto) {
        return Result.success(orderService.conditionSearch(dto));
    }

    /**
     * Admin view of order details (header + line items).
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable Long id) {
        return Result.success(orderService.details(id));
    }

    /**
     * State transitions align with the original API to keep front-end compatibility.
     */
    @PostMapping("/confirm")
    public Result<Void> confirm(@RequestParam Long id) {
        orderService.confirm(id);
        return Result.success();
    }

    @PostMapping("/rejection")
    public Result<Void> rejection(@RequestParam Long id,
                                  @RequestParam(required = false) String reason) {
        orderService.rejection(id, reason);
        return Result.success();
    }

    @PostMapping("/cancel")
    public Result<Void> cancel(@RequestParam Long id,
                               @RequestParam(required = false) String reason) {
        orderService.cancel(id, reason);
        return Result.success();
    }

    @PostMapping("/delivery")
    public Result<Void> delivery(@RequestParam Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    @PostMapping("/complete")
    public Result<Void> complete(@RequestParam Long id) {
        orderService.complete(id);
        return Result.success();
    }
}
