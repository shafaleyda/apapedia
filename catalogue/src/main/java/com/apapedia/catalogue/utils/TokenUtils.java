package com.apapedia.catalogue.utils;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class TokenUtils {

    private TokenUtils() {

    }

    private static final String SECRET_KEY = "dcf171c7e8c20f0d1de0b2268b6ac537f6f8cc5c8f73bc609ab52040dca3320d";

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static boolean isTokenExpired(String token) {
        return extractExpiration(token).after(new Date());
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static Map<String, String> decodeToken(String jwtToken) {
        Map<String, String> body = new HashMap();

        try {
            if (jwtToken != null) {
                jwtToken = jwtToken.replace("Bearer", "");
                jwtToken = jwtToken.trim();
            }

            if (StringUtils.isBlank(jwtToken)) {
                return body;
            }


            JWSObject token = JWSObject.parse(jwtToken);
            Payload tokenPayload = token.getPayload();
            JSONObject tokenBody = tokenPayload.toJSONObject();
            tokenBody.forEach((key, value) -> {
                if (Objects.isNull(value)) {
                    value = "";
                }

                body.put(key, value.toString());
            });


        } catch (Exception ex) {
            log.error("Failed to parse JWT Token. Error: {}", ex.getMessage());
        }
        return body;

    }


}
