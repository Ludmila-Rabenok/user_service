package com.example.userservice.specification;

import com.example.userservice.dto.filter.UserFilter;
import com.example.userservice.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {

  public Specification<User> build(UserFilter filter) {
    Specification<User> spec = (root, query, cb) -> cb.conjunction();
    if (filter.name() != null && !filter.name().isBlank()) {
      spec = spec.and(hasFirstName(filter.name()));
    }
    if (filter.surname() != null && !filter.surname().isBlank()) {
      spec = spec.and(hasSurname(filter.surname()));
    }
    return spec;
  }

  public Specification<User> hasFirstName(String firstName) {
    return (root, query, cb) ->
            cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
  }

  public Specification<User> hasSurname(String surname) {
    return (root, query, cb) ->
            cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
  }

}
