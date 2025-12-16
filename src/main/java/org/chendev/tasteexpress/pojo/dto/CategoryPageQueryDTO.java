package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CategoryPageQueryDTO {

    /**
     * 1-based page index coming from the client.
     */
    @Min(1)
    private int page;

    @Min(1) @Max(200)
    private int pageSize;

    private String name;

    /**
     * Optional category type filter:
     *  1 = dish category
     *  2 = set-meal category
     */
    private Integer type;
}
