package com.example.users.secret;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Generate and validate JWT token.
 */
public class JwtGenerator {

    private static final long EXPIRATION_TIME_MS = 3600000 * 2; // 2 hours
    private static final String SECRET = "zippoFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    /**
     * Generate JWT key with hmac (hash-based message authentication code is a cryptographic hash function).
     * @param provisionerId
     * @param organizationId
     * @return
     */
    public String generateJwt(Integer provisionerId, Integer organizationId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject("something")
                .claim("provId", provisionerId)
                .claim("orgId", organizationId)
                .setIssuedAt(now)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(hmacKey)
                .compact();
    }

    /**
     * Throw exception if expiration date has outdated.
     * @param jwtString
     * @return claims
     */
    public Jws<Claims> parseAndValidateJwt(String jwtString) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);
        return jwt;
    }
}
