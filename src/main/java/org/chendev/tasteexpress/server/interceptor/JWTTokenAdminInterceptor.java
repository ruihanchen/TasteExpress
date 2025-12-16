package org.chendev.tasteexpress.server.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chendev.tasteexpress.common.constant.JwtClaimsConstant;
import org.chendev.tasteexpress.common.context.BaseContext;
import org.chendev.tasteexpress.common.properties.JwtProperties;
import org.chendev.tasteexpress.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.chendev.tasteexpress.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTTokenAdminInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("[ADMIN-INTCPT] preHandle hit, uri={}", request.getRequestURI());
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 优先使用 Authorization: Bearer xxx
        String headerName = jwtProperties.getAdminTokenName(); // 你配置里的，比如 "admin-token"
        String raw = request.getHeader(headerName);
        if (raw == null) {
            raw = request.getHeader("Authorization");
        }
        log.info("[ADMIN-INTCPT] headerName={}, headerValue={}", headerName, raw);

        if (raw == null || raw.isBlank()) {
            return unauthorized(response, "missing token header");
        }

        String token = raw.startsWith("Bearer ") ? raw.substring(7) : raw;

        try {
            Long empId = tokenService.parseAdminToken(token);
            if (empId == null) {
                return unauthorized(response, "empId claim missing");
            }

            BaseContext.setCurrentId(empId);
            log.info("[ADMIN-INTCPT] set empId={} into BaseContext, PASS", empId);
            return true;
        } catch (Exception ex) {
            log.warn("[ADMIN-INTCPT] token parse failed: {}", ex.getMessage());
            return unauthorized(response, "invalid token");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        BaseContext.removeCurrentId();
    }

    private boolean unauthorized(HttpServletResponse response, String reason) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"statusCode\":401,\"message\":\"UNAUTHORIZED\"}");
        response.getWriter().flush();
        log.debug("[ADMIN-INTCPT] unauthorized: {}", reason);
        return false;
    }
}
