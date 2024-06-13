package com.visa.userService.helper;


import com.visa.lib.Utils.Constant;
import com.visa.lib.exceptions.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtHelper {
    private static final int MINUTES = 60;


    public static String generateToken(Authentication authentication) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream() // roles
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .issuedAt(Date.from(now))
                .expiration(new Date(System.currentTimeMillis() + Constant.LIFE_TIME))
                .signWith(Keys.hmacShaKeyFor(Constant.SECRET_KEY.getBytes()))
                .compact();
//        return Jwts.builder().setIssuedAt(Date.from(now)) // fecha creación
//                .setSubject(authentication.getName()) // usuario
//                .claim("authorities", authentication.getAuthorities().stream() // roles
//                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                .setExpiration(new Date(System.currentTimeMillis() + Constant.LIFE_TIME)) // fecha caducidad
//                .signWith(Keys.hmacShaKeyFor(Constant.SECRET_KEY.getBytes()))// clave y algoritmo para firma
//                .compact();
    }


    public static String extractUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private static Claims getTokenBody(String token) {
        try {
            return Jwts
                    .parser()
//                    .setSigningKey(Constant.SECRET_KEY.getBytes())
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }

    private static boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }

    private static SecretKey getSignInKey() {
        byte[] bytes = Constant.SECRET_KEY.getBytes();
        return new SecretKeySpec(bytes, "HmacSHA256");
    }
}