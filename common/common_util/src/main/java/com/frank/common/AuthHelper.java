package com.frank.common;

import jakarta.servlet.http.HttpServletRequest;

public class AuthHelper {

    public static Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JwtHelper.getUserId(token);
    }

    public static String getUserName(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JwtHelper.getUserName(token);
    }

    private AuthHelper(){}
}
