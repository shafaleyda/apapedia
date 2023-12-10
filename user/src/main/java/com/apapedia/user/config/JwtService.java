package com.apapedia.user.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.apapedia.user.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private String token;

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        extraClaims.put("userId", userDetails.getId());
        extraClaims.put("username", userDetails.getUsername());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getEmail()) // userDetails.getUsername()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 7))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public UUID extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        String userIdAsString = (String) claims.get("userId"); // Mengambil String UUID
        return UUID.fromString(userIdAsString);// Sesuaikan dengan tipe data ID pengguna yang disimpan dalam token
    }

    public String extractUserUsername(String token) {
        Claims claims = extractAllClaims(token);
        String username = (String) claims.get("username"); // Mengambil String UUID
        return username;// Sesuaikan dengan tipe data ID pengguna yang disimpan dalam token
    }

    public String getToken() {
        return token;
    }

    public String generateToken(User userDetails) {
        token = generateToken(new HashMap<>(), userDetails);
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String expireToken(String token) {
        Claims claims = extractAllClaims(token);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(extractUsername(token))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(0)) // Set expiration time to a past date to make it expired
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }    
}
