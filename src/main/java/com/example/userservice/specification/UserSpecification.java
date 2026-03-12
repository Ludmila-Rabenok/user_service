package com.example.userservice.specification;

import com.example.userservice.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
  public static Specification<User> hasFirstName(String firstName) {
    return (root, query, cb) -> {
      if (firstName == null || firstName.isBlank()) {
        return null;
      }
      return cb.like(
              cb.lower(root.get("firstName")),
              "%" + firstName.toLowerCase() + "%"
      );
    };
  }

  public static Specification<User> hasSurname(String surname) {
    return (root, query, cb) -> {
      if (surname == null || surname.isBlank()) {
        return null;
      }
      return cb.like(
              cb.lower(root.get("surname")),
              "%" + surname.toLowerCase() + "%"
      );
    };
  }
}
