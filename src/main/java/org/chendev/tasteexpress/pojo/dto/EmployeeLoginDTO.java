package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeLoginDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
