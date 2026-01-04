package com.apa.back.infra.security.configurations;

import jakarta.servlet.http.HttpServletRequest;

public class IpResolver {

    public static String resolveIp(HttpServletRequest request) {
        String fowarded = request.getHeader("x-forwarded-for");
        if (fowarded == null || fowarded.isEmpty()) {
            assert fowarded != null;
            return fowarded.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
