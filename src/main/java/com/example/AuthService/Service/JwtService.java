package com.example.AuthService.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * Responsible for JWT creating and obtaining data from JWT.
 */
@Service
public class JwtService {

    @Value("${token.secret.key}")
    String secret;

    @Value("${token.expirations}")
    Long expiration;

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Checks if token not expired
     *
     * @param token token that must be checked
     * @return result of checking
     */
    public boolean isTokenValid(String token) {
        return !getExpiration(token).before(new Date());
    }

    /**
     * Creates new token based on passed data and secret key.
     *
     * @param userDetails data that will be encoded into token
     * @return token
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date getExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

}
