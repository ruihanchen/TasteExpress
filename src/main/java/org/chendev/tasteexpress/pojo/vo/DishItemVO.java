package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DishItemVO {

    private String name;

    private String image;

    private String description;

    private BigDecimal price;

    private Integer copies;
}
