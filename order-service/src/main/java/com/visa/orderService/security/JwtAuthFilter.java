package com.visa.orderService.security;

import com.visa.lib.Utils.Constant;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                request.setAttribute("JWT_TOKEN", token);

                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);

            }
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired for request: {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(Constant.HEADER);
        if (authHeader != null && authHeader.startsWith(Constant.PREFIX)) {
            return authHeader.substring(7);
        }
        return null;
    }

    public static String getTokenFromRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return (String) request.getAttribute("JWT_TOKEN");
        }
        return null;
    }
}
