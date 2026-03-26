package com.example.userservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
  public Long getCurrentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return Long.valueOf(auth.getName());
  }

  public String getCurrentRole() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities()
            .iterator()
            .next()
            .getAuthority();
  }
}
