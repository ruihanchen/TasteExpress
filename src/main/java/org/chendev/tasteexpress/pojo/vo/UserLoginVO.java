package org.chendev.tasteexpress.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginVO {
    private Long id;

    private String email;

    private String name;

    private String token;
}