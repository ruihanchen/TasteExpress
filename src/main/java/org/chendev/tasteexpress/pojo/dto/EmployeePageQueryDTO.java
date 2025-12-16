package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class EmployeePageQueryDTO {
    private String name;

    @Min(1)
    private int page;

    @Min(1) @Max(200)
    private int pageSize;
}
