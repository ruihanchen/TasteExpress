package org.chendev.tasteexpress.pojo.vo;

import lombok.Builder;
import lombok.Data;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DishVO {

    private Long id;

    private String name;

    private Long categoryId;

    private String categoryName;

    private BigDecimal price;

    private String image;

    private String description;

    private EnabledStatus status;

    private LocalDateTime updatedAt;

    private List<DishFlavorItemVO> flavors;
}
