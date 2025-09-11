package com.example.demo.common.jwt;

import com.example.demo.common.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static com.example.demo.common.jwt.TokenValidationResult.*;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtProperties.secret().getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        long expirationMillis = now.getTime() + jwtProperties.expirationSeconds() * 1000;
        Date expiryDate = new Date(expirationMillis);

        return Jwts.builder()
                .subject(userPrincipal.getId().toString())
                .claim("email", userPrincipal.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Long userId = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserPrincipal principal = new UserPrincipal(userId, email, authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public TokenValidationResult validateTokenWithResult(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return VALID;
        } catch (ExpiredJwtException e) {
            return EXPIRED;
        } catch (SecurityException | MalformedJwtException e) {
            return MALFORMED;
        } catch (Exception e) {
            return INVALID;
        }
    }

    /**
     * OAuth state 토큰을 발급합니다.
     * @param redirectUrl 리다이렉트할 URL (null 가능)
     * @param path 리다이렉트할 경로
     * @return JWT state 토큰
     */
    public String createStateToken(String redirectUrl, String path) {
        Date now = new Date();
        long expirationMillis = now.getTime() + 10 * 60 * 1000; // 10분
        Date expiryDate = new Date(expirationMillis);

        return Jwts.builder()
                .subject("oauth-state")
                .claim("redirect", redirectUrl)
                .claim("path", path != null ? path : "/")
                .claim("nonce", UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * OAuth state 토큰을 검증하고 클레임을 반환합니다.
     * @param token JWT state 토큰
     * @return 검증된 클레임
     * @throws IllegalArgumentException 토큰이 유효하지 않은 경우
     */
    public Claims verifyStateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("State token is null or empty");
        }

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid state token", e);
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
} 