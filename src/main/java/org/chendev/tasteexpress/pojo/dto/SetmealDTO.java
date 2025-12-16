package org.chendev.tasteexpress.pojo.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.pojo.entity.SetmealDish;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class SetmealDTO {
    private Long id;

    @NotNull
    private Long categoryId;

    @NotBlank @Size(max = 32)
    private String name;

    @DecimalMin("0.00")
    private BigDecimal price;

    @NotNull
    private EnabledStatus status;      // Enable/Disable

    @Size(max=255)
    private String description;

    @Size(max=255)
    private String image;

    @Valid
    private List<@Valid SetmealDish> setmealDishes = new ArrayList<>();
    ;
}
