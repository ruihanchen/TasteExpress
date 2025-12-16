package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class DishFlavorDTO {

    @NotBlank
    @Size(max = 32)
    private String name;

    @Size(max = 255)
    private String value;
}
