package com.visa.productService.helper;


import com.visa.lib.Utils.Constant;
import com.visa.lib.exceptions.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtHelper {

//    public static String generateToken(String email) {
//        var now = Instant.now();
//        return Jwts.builder()
//                .subject(email)
//                .issuedAt(Date.from(now))
//                .expiration(Date.from(now.plus(Constant.LIFE_TIME, ChronoUnit.MINUTES)))
//                .signWith(SignatureAlgorithm.HS256, Constant.SECRET_KEY)
//                .compact();
//    }

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