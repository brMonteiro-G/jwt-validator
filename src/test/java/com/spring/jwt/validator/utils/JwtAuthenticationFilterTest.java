package com.spring.jwt.validator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jwt.validator.exception.InvalidClaimNameException;
import com.spring.jwt.validator.exception.InvalidJwtException;
import com.spring.jwt.validator.exception.OversizeClaimException;
import com.spring.jwt.validator.exception.configuration.DelegatedAuthenticationEntryPoint;
import com.spring.jwt.validator.service.JwtService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private DelegatedAuthenticationEntryPoint handlerExceptionResolver;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    void testDoFilterInternal_WithValidJWT() throws ServletException, IOException, JsonProcessingException {
        // Setup
        String jwt = "Bearer valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn(jwt);
        Map<String, String> claims = new HashMap<>();
        claims.put("Name", "TestUser");
        when(objectMapper.readValue(any(String.class), eq(HashMap.class))).thenReturn((HashMap) claims);
        when(jwtService.extractUsername(anyString())).thenReturn("test@example.com");
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    //@Test
    void testDoFilterInternal_WithInvalidJWT() throws ServletException, IOException {
        String jwt = "Bearer eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
        when(request.getHeader("Authorization")).thenReturn(jwt);

        doThrow(new MalformedJwtException("Malformed JWT")).when(jwtService).extractUsername(anyString());
        assertThrows(InvalidJwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        verify(handlerExceptionResolver).commence(any(HttpServletRequest.class), any(HttpServletResponse.class), any(InvalidJwtException.class));
    }

    //@Test
    void testIsClaimValid_WithInvalidClaimName() {
        Map<String, String> claims = new HashMap<>();
        claims.put("Name", "Test123");

        // assertThrows(InvalidClaimNameException.class, () -> jwtAuthenticationFilter.isClaimValid(claims));
    }

    //@Test
    void testIsClaimValid_WithOversizeClaims() {
        Map<String, String> claims = new HashMap<>();
        claims.put("Name", "TestUser");
        claims.put("Role", "User");
        claims.put("Extra", "ExtraValue");
        claims.put("Additional", "AnotherExtra");

        //assertThrows(OversizeClaimException.class, () -> jwtAuthenticationFilter.isClaimValid(claims));
    }

    //@Test
    void testGetClaims_ValidToken() throws JsonProcessingException {
        String token = "header." + Base64.getEncoder().encodeToString("{\"Name\":\"TestUser\"}".getBytes()) + ".signature";

        //  Map<String, String> claims = jwtAuthenticationFilter.getClaims(token);

        //  assertEquals("TestUser", claims.get("Name"));
    }

}