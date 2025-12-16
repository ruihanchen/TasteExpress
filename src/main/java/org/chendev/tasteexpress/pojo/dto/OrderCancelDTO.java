package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class OrderCancelDTO {

    private Long id;

    private String cancelReason;

}
