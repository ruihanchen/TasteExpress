package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class OrderPageQueryDTO {

    private Integer page;

    private Integer pageSize;

    private String orderNumber;

    private String phone;

    private Integer status;

    private String beginTime;

    private String endTime;
}
