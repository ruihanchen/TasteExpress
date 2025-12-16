package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishOverviewVO {

    private long onSaleCount;

    private long offSaleCount;

}
