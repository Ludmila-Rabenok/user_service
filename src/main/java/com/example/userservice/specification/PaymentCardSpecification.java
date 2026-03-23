package com.example.userservice.specification;

import com.example.userservice.dto.filter.PaymentCardFilter;
import com.example.userservice.entity.PaymentCard;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaymentCardSpecification {

  public Specification<PaymentCard> build(PaymentCardFilter filter) {
    Specification<PaymentCard> spec = (root, query, cb) -> cb.conjunction();
    if (filter.userName() != null && !filter.userName().isBlank()) {
      spec = spec.and(hasUserName(filter.userName()));
    }
    if (filter.userSurname() != null && !filter.userSurname().isBlank()) {
      spec = spec.and(hasUserSurname(filter.userSurname()));
    }
    if (filter.active() != null) {
      spec = spec.and(isActive(filter.active()));
    }
    return spec;
  }

  public Specification<PaymentCard> hasUserName(String name) {
    return (root, query, cb) ->
            cb.like(
                    cb.lower(root.join("user").get("name")),
                    "%" + name.toLowerCase() + "%"
            );
  }

  public Specification<PaymentCard> hasUserSurname(String surname) {
    return (root, query, cb) ->
            cb.like(
                    cb.lower(root.join("user").get("surname")),
                    "%" + surname.toLowerCase() + "%"
            );
  }

  public Specification<PaymentCard> isActive(Boolean active) {
    return (root, query, cb) ->
            cb.equal(root.get("active"), active);
  }
}