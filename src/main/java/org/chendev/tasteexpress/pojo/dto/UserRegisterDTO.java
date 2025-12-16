package org.chendev.tasteexpress.pojo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6, max = 64)
    private String password;

    @NotBlank @Size(max = 32)
    private String name;

    @Size(max = 20)
    private String phone;
}
