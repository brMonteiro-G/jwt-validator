package com.spring.jwt.validator.service;

import com.spring.jwt.validator.exception.InvalidClaimNameException;
import com.spring.jwt.validator.exception.OversizeClaimException;
import com.spring.jwt.validator.model.DTO.UserDTO;
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
import java.util.regex.Pattern;

import static com.spring.jwt.validator.service.AuthenticationService.grantSeed;

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

        return isClaimValid(claims) ? claimsResolver.apply(claims): null;
    }

    public String generateToken(UserDTO userDto) {

        return buildToken(userDto, jwtExpiration);
    }


    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            UserDTO userDto,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(userDto.getRole())
                .setClaims(userDto.getSeed())
                .setClaims(userDto.getName())
                .setSubject(userDto.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
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


    private boolean isClaimValid(Claims claims) {

        String nameClaim = claims.get("Name", String.class);
        if (Pattern.compile("[0-9]").matcher(nameClaim).find()) {
            throw new InvalidClaimNameException("Claim 'Name' must have only alphabetic letter.");
        }

        if (claims.size() > 3) {
            throw new OversizeClaimException("JWT cannot contains more than 3 claims");
        }

        return true;
    }


}