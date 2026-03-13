package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.dto.filter.UserFilter;
import com.example.userservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<Page<UserResponseDto>> getAll(UserFilter filter, Pageable pageable) {
    return ResponseEntity.ok(userService.getAll(filter, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getById(id));
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
    UserResponseDto created = userService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> update(@PathVariable Long id,
                                                @RequestBody UserUpdateDto dto) {
    return ResponseEntity.ok(userService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<Void> activate(@PathVariable Long id) {
    userService.activate(id);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivate(@PathVariable Long id) {
    userService.deactivate(id);
    return ResponseEntity.ok().build();
  }
}