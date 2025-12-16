package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class OrderRejectionDTO {
    private Long id;

    private String rejectionReason;
}
