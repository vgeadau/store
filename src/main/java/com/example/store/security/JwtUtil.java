package com.example.store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtUtil component used on token related operations.
 */
@Component
public class JwtUtil {

    private final static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final static int TEN_HOURS = 1000 * 60 * 60 * 10;

    /**
     * Extracts username from token.
     * @param token String
     * @return the username String
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts expiration date from token.
     * @param token String
     * @return the expiration Date object
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts claim from token
     * @param token String
     * @param claimsResolver Function
     * @return T claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from token
     * @param token String
     * @return Claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if Token is Expired.
     * @param token String
     * @return TRUE if is expired, FALSE otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Method responsible for generating a token.
     * @param userDetails UserDetails
     * @return String LOGIN_TOKEN
     */
    public String generateToken(UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Method that creates a token using the SECRET_KEY.
     * @param claims Map
     * @param subject String
     * @return token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TEN_HOURS))
                .signWith(SECRET_KEY).compact();
    }

    /**
     * Method that validates a token.
     * @param token String
     * @param userDetails UserDetails
     * @return TRUE if is valid, FALSE otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}