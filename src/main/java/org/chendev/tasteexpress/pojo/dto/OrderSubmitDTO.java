package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class OrderSubmitDTO {
    private Long addressBookId;

    private Integer payMethod;

    private String remark;
}