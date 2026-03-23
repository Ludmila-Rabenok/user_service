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
      spec = spec.and(hasName(filter.name()));
    }
    if (filter.surname() != null && !filter.surname().isBlank()) {
      spec = spec.and(hasSurname(filter.surname()));
    }
    if (filter.active() != null) {
      spec = spec.and(isActive(filter.active()));
    }
    return spec;
  }

  public Specification<User> hasName(String name) {
    return (root, query, cb) ->
            cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }

  public Specification<User> hasSurname(String surname) {
    return (root, query, cb) ->
            cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
  }

  public Specification<User> isActive(Boolean active) {
    return (root, query, cb) ->
            cb.equal(root.get("active"), active);
  }
}