package com.visa.orderService.security;

import com.visa.lib.Utils.Constant;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtProvider {

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        List<GrantedAuthority> authorities = extractAuthorities(claims);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    private List<GrantedAuthority> extractAuthorities(Claims claims) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("authorities");

        if (roles != null) {
            roles.forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });
        }

        return authorities;
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid format Token -> Message: ", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token -> Message: ", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token -> Message: ", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: ", e);
        }
        return false;
    }

    private static SecretKey getSignInKey() {
        byte[] bytes = Constant.SECRET_KEY.getBytes();
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

}
