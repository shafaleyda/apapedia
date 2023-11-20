package com.apapedia.catalogue.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApiScope {

    private ApiScope() {}

    public static void validateAuthority(String token, Set<String> roles) {

        Map<String, String> claims = TokenUtils.decodeToken(token);
        String access = claims.get("role");

        if (StringUtils.isBlank(access) || !roles.contains(access)) {
            throw new RuntimeException();
        }

        boolean isValid = TokenUtils.isTokenExpired(token);
        if (!isValid) {
            throw new RuntimeException();
        }

    }
}
