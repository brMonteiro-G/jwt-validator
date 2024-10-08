package com.spring.jwt.validator.utils;

import com.fasterxml.jackson.core.JsonParseException;
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
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final DelegatedAuthenticationEntryPoint handlerExceptionResolver;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

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

            var claims = this.getClaims(jwt).orElseThrow(() -> new MalformedJwtException("Invalid Jwt"));
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

            this.handleException(request, response, exception);

        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws IOException, ServletException {
        if (exception instanceof MalformedJwtException || exception instanceof JsonParseException) {
            handlerExceptionResolver.commence(request, response, new InvalidJwtException("Invalid or malformed JWT token"));
        } else if (exception instanceof ExpiredJwtException) {
            handlerExceptionResolver.commence(request, response, new InvalidJwtException("Expired JWT"));
        } else if (exception instanceof OversizeClaimException) {
            handlerExceptionResolver.commence(request, response, new InvalidJwtException("Invalid claims"));
        } else if (exception instanceof InvalidClaimNameException) {
            handlerExceptionResolver.commence(request, response, new InvalidJwtException("Invalid claim name"));
        } else {
            logger.error("Error filtering JWT token", exception);
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


    private Optional<HashMap<String, String>> getClaims(String token) throws JsonProcessingException {
        String[] tokenParts = token.split("\\.");
        String payload = tokenParts[1];

        byte[] decodedBytes = Base64.getDecoder().decode(payload);
        String decodedPayload = new String(decodedBytes);

        HashMap<String, String> map = objectMapper.readValue(decodedPayload, HashMap.class);
        return Optional.ofNullable(map);
    }

}
