package org.chendev.tasteexpress.pojo.vo;

import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.pojo.entity.SetmealDish;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetmealVO {
    private Long id;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private EnabledStatus status;

    private String description;

    private String image;

    private LocalDateTime updatedAt;

    private String categoryName;

    private List<SetmealDish> setmealDishes;
}
