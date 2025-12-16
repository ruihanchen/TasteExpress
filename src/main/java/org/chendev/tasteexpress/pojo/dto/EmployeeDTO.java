package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class EmployeeDTO {

    private Long id;

    @NotBlank @Size(max = 64)
    private String username;

    @NotBlank @Size(max = 64)
    private String password;

    @NotBlank @Size(max = 32)
    private String name;

    @NotBlank @Size(max = 32)
    private String phone;

    private String sex;

    private String idNumber;
}
