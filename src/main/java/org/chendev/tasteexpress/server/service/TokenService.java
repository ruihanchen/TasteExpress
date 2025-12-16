package org.chendev.tasteexpress.server.service;

public interface TokenService {
    String createAdminToken(Long empId);
    Long parseAdminToken(String token);

    String createUserToken(Long userId);
    Long parseUserToken(String token);
}
