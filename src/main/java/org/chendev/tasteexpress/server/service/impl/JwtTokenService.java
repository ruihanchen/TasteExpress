package org.chendev.tasteexpress.server.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.chendev.tasteexpress.common.constant.JwtClaimsConstant;
import org.chendev.tasteexpress.common.properties.JwtProperties;
import org.chendev.tasteexpress.common.utils.JwtUtil;
import org.chendev.tasteexpress.server.service.TokenService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

    private final JwtProperties jwtProperties;

    @Override
    public String createAdminToken(Long empId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, empId);
        return JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );
    }

    @Override
    public String createUserToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userId);
        return JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );
    }

    @Override
    public Long parseAdminToken(String token) {
        Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
        Object id = claims.get(JwtClaimsConstant.EMP_ID);
        return id == null ? null : Long.valueOf(id.toString());
    }

    @Override
    public Long parseUserToken(String token) {
        Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
        Object id = claims.get(JwtClaimsConstant.USER_ID);
        return id == null ? null : Long.valueOf(id.toString());
    }
}
