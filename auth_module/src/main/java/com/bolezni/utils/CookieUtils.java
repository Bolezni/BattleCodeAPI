package com.bolezni.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public final class CookieUtils {
    private CookieUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void createCookie(HttpServletResponse response, String name, String value) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(name, value)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie responseCookie = ResponseCookie.from(name)
                .maxAge(0)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }


    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
