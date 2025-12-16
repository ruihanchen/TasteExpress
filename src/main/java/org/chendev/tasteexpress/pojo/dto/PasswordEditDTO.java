package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

@Data
public class PasswordEditDTO {

    //员工id
    private Long empId;

    //旧密码
    private String oldPassword;

    //新密码
    private String newPassword;

}
