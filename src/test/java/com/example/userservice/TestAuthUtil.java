package com.example.userservice;

import com.example.userservice.security.AuthUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestAuthUtil {
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final String ROLE_USER = "ROLE_USER";

  public static void mockAuth(Long userId, String role) {
    AuthUserDetails principal = new AuthUserDetails(userId, role);
    Authentication auth = new UsernamePasswordAuthenticationToken(
            principal, null, principal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}