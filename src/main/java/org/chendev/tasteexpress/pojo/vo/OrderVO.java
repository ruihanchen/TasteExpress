package org.chendev.tasteexpress.pojo.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chendev.tasteexpress.pojo.entity.Order;
import org.chendev.tasteexpress.pojo.entity.OrderDetail;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO extends Order {

    //订单菜品信息
    private String orderDishes;

    //订单详情
    private List<OrderDetail> orderDetailList;

}
