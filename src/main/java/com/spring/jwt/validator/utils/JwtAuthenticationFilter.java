package com.spring.jwt.validator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.validator.exception.InvalidClaimNameException;
import com.spring.jwt.validator.exception.InvalidJwtException;
import com.spring.jwt.validator.exception.OversizeClaimException;
import com.spring.jwt.validator.exception.configuration.DelegatedAuthenticationEntryPoint;
import com.spring.jwt.validator.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private DelegatedAuthenticationEntryPoint handlerExceptionResolver;
    @Autowired

    private JwtService jwtService;
    @Autowired

    private UserDetailsService userDetailsService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);

            var claims = this.getClaims(jwt);
            final String userEmail = isClaimValid(claims) ? jwtService.extractUsername(jwt) : null;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {

            if (exception instanceof MalformedJwtException) {

                handlerExceptionResolver.commence(request, response, new InvalidJwtException("invalid or malformed JWT token"));
            }

            if (exception instanceof ExpiredJwtException) {
                handlerExceptionResolver.commence(request, response, new InvalidJwtException("expired JWT"));
            }

            if (exception instanceof OversizeClaimException) {
                handlerExceptionResolver.commence(request, response, new InvalidJwtException("invalid claims"));
            }

            if (exception instanceof InvalidClaimNameException) {
                handlerExceptionResolver.commence(request, response, new InvalidJwtException("invalid claim name"));
            }

            logger.error("there is an error while filter JWT token");
//            throw new RuntimeException();

        }
    }

    private boolean isClaimValid(Map<String, String> claims) {

        String nameClaim = claims.get("Name");
        if (Pattern.compile("[0-9]").matcher(nameClaim).find()) {
            throw new InvalidClaimNameException("Claim 'Name' must have only alphabetic letter.");
        }

        if (claims.size() > 3) {
            throw new OversizeClaimException("JWT cannot contains more than 3 claims");
        }

        return true;
    }


    private HashMap<String, String> getClaims(String token) throws JsonProcessingException {
        String[] tokenParts = token.split("\\.");
        String payload = tokenParts[1];

        byte[] decodedBytes = Base64.getDecoder().decode(payload);
        String decodedPayload = new String(decodedBytes);

        HashMap<String, String> map = objectMapper.readValue(decodedPayload, HashMap.class);
        return map;
    }

}
