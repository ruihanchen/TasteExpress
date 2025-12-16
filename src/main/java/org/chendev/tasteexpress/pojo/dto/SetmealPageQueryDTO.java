package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;

@Data
public class SetmealPageQueryDTO {

    @Min(1)
    private int page;

    @Min(1) @Max(200)
    private int pageSize;

    private String name;

    private Long categoryId;

    @NotNull
    private EnabledStatus status;
}
