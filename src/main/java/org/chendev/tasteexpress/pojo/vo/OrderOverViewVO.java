package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderOverViewVO {

    private Integer waitingOrders;

    private Integer deliveredOrders;

    private Integer completedOrders;

    private Integer cancelledOrders;

    private Integer allOrders;
}
