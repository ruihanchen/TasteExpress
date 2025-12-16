package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OrderReportVO {

    private String dateList;

    private String orderCountList;

    private String validOrderCountList;

    private Integer totalOrderCount;

    private Integer validOrderCount;

    private Double orderCompletionRate;
}