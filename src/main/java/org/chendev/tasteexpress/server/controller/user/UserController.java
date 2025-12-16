package org.chendev.tasteexpress.server.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.result.Result;
import org.chendev.tasteexpress.pojo.dto.UserLoginDTO;
import org.chendev.tasteexpress.pojo.dto.UserRegisterDTO;
import org.chendev.tasteexpress.pojo.vo.UserLoginVO;
import org.chendev.tasteexpress.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Register a new user with email + password.
     * Returns a JWT so the client can treat this as "sign up + sign in".
     */
    @PostMapping("/register")
    public Result<UserLoginVO> register(@Valid @RequestBody UserRegisterDTO dto) {
        log.info("User registration request, email={}", dto.getEmail());
        return Result.success(userService.register(dto));
    }

    /**
     * Login with email + password.
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        log.info("User login request, email={}", dto.getEmail());
        return Result.success(userService.login(dto));
    }
}