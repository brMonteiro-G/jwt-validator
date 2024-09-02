package com.spring.jwt.validator.service;

import com.spring.jwt.validator.model.DTO.UserDTO;
import com.spring.jwt.validator.model.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;


    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public LoginResponse generatedAuthenticatedUser(UserDTO userDto) {
        var token = buildToken(userDto, jwtExpiration);

        return LoginResponse.builder().token(token).expiresIn(jwtExpiration).build();
    }


    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            UserDTO userDto,

            //TODO: add expiration
            long expiration
    ) {
        Map<String, String> mapClaims = new HashMap();

        //TODO: improve it later
        mapClaims.put("Name", userDto.getName().get("Name").toString());
        mapClaims.put("Seed", userDto.getSeed().get("Seed").toString());
        mapClaims.put("Role", userDto.getRole().get("ROLE").toString());

        return Jwts
                .builder()
                .setClaims(mapClaims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        //TODO: isn´t a good practice use a human readble seed, usually we use base 64 secret, but here we go
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}