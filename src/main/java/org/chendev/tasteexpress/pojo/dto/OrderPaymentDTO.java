package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class OrderPaymentDTO {

    private String orderNumber;

    private Integer payMethod;
}
