package org.chendev.tasteexpress.server.service;

import org.chendev.tasteexpress.pojo.dto.UserLoginDTO;
import org.chendev.tasteexpress.pojo.dto.UserRegisterDTO;
import org.chendev.tasteexpress.pojo.vo.UserLoginVO;

public interface UserService {
    // Register a new customer account and immediately sign them in
    UserLoginVO register(UserRegisterDTO dto);

    // Login with email + password
    UserLoginVO login(UserLoginDTO dto);

}
