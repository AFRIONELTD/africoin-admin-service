package com.afrione.africoinservice.infrastructure.serviceimpl;

import com.afrione.africoinservice.domain.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class JWTServiceImpl implements JWTService {

    private final String issuer;
    private final int clockSkewSec;

    public JWTServiceImpl(@Value("${TOKEN_ISSUER}") String issuer) {
        this.issuer = issuer;
        this.clockSkewSec = 300;
    }

    @Override
    public String expiringToken(String secretKey, Map<String, String> attributes, int minutes) {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedKey));

        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> claims = new HashMap<>(attributes);
        claims.put(Claims.ISSUER, issuer);
        claims.put(Claims.ISSUED_AT, toDate(now));

        if (minutes > 0) {
            LocalDateTime expiresAt = now.plusMinutes(minutes);
            claims.put(Claims.EXPIRATION, toDate(expiresAt));
        }

        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    @Override
    public Map<String, String> verify(String secretKey, String token) {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodedKey));

        JwtParser parser = Jwts.parser()
                .requireIssuer(issuer)
                .clockSkewSeconds(clockSkewSec)
                .verifyWith(key)
                .build();

        return parseClaims(() -> parser.parseSignedClaims(token).getPayload());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        Map<String, String> stringMap = new HashMap<>();
        try {
            final Claims claims = toClaims.get();
            for (final Map.Entry<String, Object> e: claims.entrySet()) {
                stringMap.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return stringMap;
        } catch (final IllegalArgumentException | JwtException e) {
            return stringMap;
        }
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}