// Copyright (c) ShortThirdMan 2025.
package com.shortthirdman.primekit.essentials.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public final class JwtUtils {

    private JwtUtils() {}

    /**
     * @return the decoded JWT secret in
     */
    public static SecretKey getSecretKey() {
        var jwtSecret = System.getProperty("jwt.secret");
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        var jwtSecret = System.getProperty("jwt.secret");
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .and()
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * @param token the token to validate
     * @return true if valid, else false
     */
    public static boolean validateTokenExpiration(String token) {
        var jwtSecret = System.getProperty("jwt.secret");
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        Claims claims = Jwts.parser().verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration().after(new Date());
    }

    /**
     * @param token the token to validate
     * @return true if valid, else false
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch(SecurityException | MalformedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        } catch (ExpiredJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT token compact of handler are invalid.");
        }
    }
}
