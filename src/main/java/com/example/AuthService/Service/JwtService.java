package com.example.AuthService.Service;

import com.example.AuthService.DTO.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for JWT creating and obtaining data from JWT.
 */
@Getter
@Setter
@Service
public class JwtService {

    @Value("${token.secret.key}")
    private String secret;

    @Value("${token.expirations}")
    private Long expiration;

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
     * @param user data that will be encoded into token
     * @return token
     */
    public String generateToken(User user) {
        Map<String, List<String>> claims = new HashMap<>();
        claims.put("roles", user.getRolesOnly());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
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
