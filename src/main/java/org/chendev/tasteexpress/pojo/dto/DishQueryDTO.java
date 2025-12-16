package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;

/**
 * Query params for dish paging / filtering.
 */
@Data
public class DishQueryDTO {

    @Min(1)
    private Integer page = 1;

    @Min(1) @Max(200)
    private Integer pageSize = 10;

    private String name;

    private Long categoryId;

    @NotNull
    private EnabledStatus status;
}
