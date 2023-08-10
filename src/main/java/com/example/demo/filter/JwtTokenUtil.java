package com.example.demo.filter;

import java.security.Key;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenUtil {

    private static final String SECRET_KEY = "5A7134743777397A24432646294A404E635266556A586E3272357538782F4125";

    public boolean validate(String token, UserDetails userDetails) {
        final var username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails);
    }

    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(SignatureAlgorithm.HS256, key())
            .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Claims getAllClaims(String token) {
        return Jwts
            .parser()
            .setSigningKey(key())
            .parseClaimsJws(token)
            .getBody();

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }
}
