package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  @EntityGraph(attributePaths = "cards")
  Optional<User> findById(Long id);

  @Lock(PESSIMISTIC_WRITE)
  @Query("SELECT u FROM User u WHERE u.id = :id")
  Optional<User> findByIdForUpdate(Long id);

  @Override
  @EntityGraph(attributePaths = "cards")
  Page<User> findAll(Specification<User> spec, Pageable pageable);
}