package com.example.userservice.specification;

import com.example.userservice.entity.PaymentCard;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {

  public static Specification<PaymentCard> hasUserFirstName(String firstName) {
    return (root, query, cb) -> {
      if (firstName == null || firstName.isBlank()) {
        return null;
      }
      return cb.like(
              cb.lower(root.join("user").get("firstName")),
              "%" + firstName.toLowerCase() + "%"
      );
    };
  }

  public static Specification<PaymentCard> hasUserSurname(String surname) {
    return (root, query, cb) -> {
      if (surname == null || surname.isBlank()) {
        return null;
      }
      return cb.like(
              cb.lower(root.join("user").get("surname")),
              "%" + surname.toLowerCase() + "%"
      );
    };
  }
}
