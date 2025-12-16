package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class OrderConfirmDTO {

    private Long id;

    private Integer status; // change to enum OrderStatus

}