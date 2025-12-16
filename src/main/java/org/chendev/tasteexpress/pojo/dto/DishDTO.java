package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Payload for creating or updating a dish.
 */
@Data
public class DishDTO {

    private Long id;

    @NotBlank
    @Size(max = 32)
    private String name;

    @NotNull
    private Long categoryId;

    @DecimalMin(value = "0.00")
    private BigDecimal price;

    @Size(max = 255)
    private String image;

    @Size(max = 255)
    private String description;

    @NotNull
    private EnabledStatus status;

    @Valid
    private List<@Valid DishFlavorDTO> flavors = new ArrayList<>();
}
