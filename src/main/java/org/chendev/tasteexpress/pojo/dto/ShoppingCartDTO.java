package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class ShoppingCartDTO {

    private Long dishId;

    private Long setmealId;

    private String dishFlavor;
}