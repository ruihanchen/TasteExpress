package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CategoryDTO {

    private Long id;

    @NotNull
    @Min(1) @Max(2)
    private Integer type;

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotNull
    @Min(0)
    private Integer sort;
}
