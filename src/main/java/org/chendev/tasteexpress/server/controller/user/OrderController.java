package org.chendev.tasteexpress.server.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.dto.OrderSubmitDTO;
import org.chendev.tasteexpress.pojo.vo.OrderSubmitVO;
import org.chendev.tasteexpress.pojo.vo.OrderVO;
import org.chendev.tasteexpress.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Place an order from the current user's cart.
     * Minimal happy-path: we snapshot address and cart lines, compute totals, then clear the cart.
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO dto) {
        return Result.success(orderService.submit(dto));
    }

    /**
     * Paginated history for the current user.
     * Optional status filter keeps the interface symmetric with the original project.
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> history(@RequestParam Integer page,
                                      @RequestParam Integer pageSize,
                                      @RequestParam(required = false) Integer status) {
        return Result.success(orderService.pageQueryUser(page, pageSize, status));
    }

    /**
     * Order details including line items.
     * Returns null if not found, consistent with the project's earlier conventions.
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> detail(@PathVariable Long id) {
        return Result.success(orderService.details(id));
    }
}
