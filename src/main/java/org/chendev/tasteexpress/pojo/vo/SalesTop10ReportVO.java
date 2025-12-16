package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class SalesTop10ReportVO {

    //商品名称列表，以逗号分隔，例如：鱼香肉丝,宫保鸡丁,水煮鱼
    private String nameList;

    //销量列表，以逗号分隔，例如：260,215,200
    private String numberList;

}
