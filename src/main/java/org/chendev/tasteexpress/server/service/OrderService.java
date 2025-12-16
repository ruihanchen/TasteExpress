package org.chendev.tasteexpress.server.service;

import org.chendev.tasteexpress.common.result.PageResult;
import org.chendev.tasteexpress.pojo.dto.OrderPageQueryDTO;
import org.chendev.tasteexpress.pojo.dto.OrderSubmitDTO;
import org.chendev.tasteexpress.pojo.vo.OrderSubmitVO;
import org.chendev.tasteexpress.pojo.vo.OrderVO;

public interface OrderService {

    OrderSubmitVO submit(OrderSubmitDTO dto);

    // 用户端：历史订单分页
    PageResult pageQueryUser(Integer page, Integer pageSize, Integer status);

    // for user panel
    OrderVO details(Long id);

    // for admin panel
    PageResult conditionSearch(OrderPageQueryDTO dto);

    // for admin panel
    void confirm(Long id);            // confirmed order

    void rejection(Long id, String reason);

    void cancel(Long id, String reason);

    void delivery(Long id);           //

    void complete(Long id);           // completed order
}
