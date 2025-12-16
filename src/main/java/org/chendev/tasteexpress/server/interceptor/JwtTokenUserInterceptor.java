package org.chendev.tasteexpress.server.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.constant.JwtClaimsConstant;
import org.chendev.tasteexpress.common.context.BaseContext;
import org.chendev.tasteexpress.common.properties.JwtProperties;
import org.chendev.tasteexpress.common.utils.JwtUtil;
import org.chendev.tasteexpress.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("[USER-INTCPT] preHandle hit, uri={}", request.getRequestURI());
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String uri = request.getRequestURI();

        // 对 user 登录/注册放行
        if (uri.startsWith("/user/login") || uri.startsWith("/user/register")) {
            return true;
        }

        String headerName = jwtProperties.getUserTokenName(); // 比如 "user-token"
        String raw = request.getHeader(headerName);
        if (raw == null) {
            raw = request.getHeader("Authorization");
        }
        log.info("[USER-INTCPT] headerName={}, headerValue={}", headerName, raw);

        if (raw == null || raw.isBlank()) {
            return unauthorized(response);
        }

        String token = raw.startsWith("Bearer ") ? raw.substring(7) : raw;

        try {
            Long userId = tokenService.parseUserToken(token);
            if (userId == null) {
                return unauthorized(response);
            }

            BaseContext.setCurrentId(userId);
            log.info("[USER-INTCPT] userId set: {}", userId);
            return true;
        } catch (Exception ex) {
            log.warn("[USER-INTCPT] token parse failed: {}", ex.getMessage());
            return unauthorized(response);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest req,
                                HttpServletResponse res,
                                Object handler,
                                Exception ex) {
        BaseContext.removeCurrentId();
    }

    private boolean unauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"statusCode\":401,\"message\":\"UNAUTHORIZED\"}");
        response.getWriter().flush();
        return false;
    }
}
