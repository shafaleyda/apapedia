package com.apapedia.catalogue.utils;

import com.apapedia.catalogue.exception.ExtendException;
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
            throw new ExtendException("40001", "Gagal connect", "Tidak Memiliki Akses");
        }

        boolean isValid = TokenUtils.isTokenExpired(token);
        if (!isValid) {
            throw new ExtendException("40002", "Gagal connect", "Token Expired");
        }

    }
}
