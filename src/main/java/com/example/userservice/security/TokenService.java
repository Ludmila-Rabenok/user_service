package com.example.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class TokenService {

  private static final String CLAIM_ROLE = "role";
  private static final String CLAIM_TOKEN_TYPE = "token_type";
  private final SecretKey secretKey;

  public TokenService(@Value("${jwt.secret}") String secret) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public boolean isAccessTokenValid(String token) {
    try {
      Claims claims = parseClaims(token);
      String type = claims.get(CLAIM_TOKEN_TYPE, String.class);
      return "ACCESS".equals(type);
    } catch (Exception e) {
      return false;
    }
  }

  public Long getUserId(String token) {
    return Long.valueOf(parseClaims(token).getSubject());
  }

  public String getRole(String token) {
    return parseClaims(token).get(CLAIM_ROLE, String.class);
  }

  private Claims parseClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
  }
}