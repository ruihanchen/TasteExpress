package org.chendev.tasteexpress.pojo.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;

@Data
@Builder
public class CategoryVO {

    private Long id;

    private Integer type;

    private String name;

    private Integer sort;

    private EnabledStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
