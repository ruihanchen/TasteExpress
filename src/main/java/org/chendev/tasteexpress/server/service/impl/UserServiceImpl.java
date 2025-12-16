package org.chendev.tasteexpress.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.enumeration.EnabledStatus;
import org.chendev.tasteexpress.common.error.ErrorCode;
import org.chendev.tasteexpress.common.exception.AuthException;
import org.chendev.tasteexpress.pojo.dto.UserLoginDTO;
import org.chendev.tasteexpress.pojo.dto.UserRegisterDTO;
import org.chendev.tasteexpress.pojo.entity.User;
import org.chendev.tasteexpress.pojo.vo.UserLoginVO;
import org.chendev.tasteexpress.server.repository.UserRepository;
import org.chendev.tasteexpress.server.service.TokenService;
import org.chendev.tasteexpress.server.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    @Transactional
    public UserLoginVO register(UserRegisterDTO dto) {
        String normalizedEmail = dto.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {

            throw new IllegalArgumentException("Email is already registered.");
        }

        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .email(normalizedEmail)
                .phone(dto.getPhone())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .status(EnabledStatus.ENABLED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        user = userRepository.save(user);

        String token = tokenService.createUserToken(user.getId());

        log.info("New user registered, id={}, email={}", user.getId(), user.getEmail());

        return UserLoginVO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public UserLoginVO login(UserLoginDTO dto) {
        String normalizedEmail = dto.getEmail().trim().toLowerCase();

        //
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_ACCOUNT_NOT_FOUND));

        if (EnabledStatus.ENABLED.equals(user.getStatus())) {
            throw new AuthException(ErrorCode.AUTH_ACCOUNT_LOCKED);
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.AUTH_PASSWORD_ERROR);
        }

        user.setLastLoginAt(LocalDateTime.now());

        userRepository.save(user);

        String token = tokenService.createUserToken(user.getId());

        return UserLoginVO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .token(token)
                .build();
    }
}