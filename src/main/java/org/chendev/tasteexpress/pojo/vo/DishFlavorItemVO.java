package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DishFlavorItemVO {

    private String name;
    private String value;
}
