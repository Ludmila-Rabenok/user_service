package com.example.userservice.integration.controller;

import com.example.userservice.TestAuthUtil;
import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.entity.User;
import com.example.userservice.integration.AbstractIntegrationTest;
import com.example.userservice.integration.TestData;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerIT extends AbstractIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void getAll_shouldReturnAllUsersWithCards_whenNotParameter() throws Exception {
    List<User> users = TestData.buildUsersWithCards(3);
    userRepository.saveAll(users);
    TestAuthUtil.mockAuth(4L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(3))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].cards").isArray());
  }

  @Test
  void getAll_shouldReturnActiveUsersWithCards_whenParameterActiveTrue() throws Exception {
    List<User> users = TestData.buildUsersWithCards(3);
    users.get(1).setActive(false);
    userRepository.saveAll(users);
    TestAuthUtil.mockAuth(4L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(get("/api/users")
                    .param("active", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].active").value(true))
            .andExpect(jsonPath("$.content[1].active").value(true))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].cards").isArray());
  }

  @Test
  void getAll_shouldReturnUsersWithCards_whenParameterPageAndSize() throws Exception {
    List<User> users = TestData.buildUsersWithCards(6);
    userRepository.saveAll(users);
    TestAuthUtil.mockAuth(7L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(get("/api/users")
                    .param("page", "0")
                    .param("size", "3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(3))
            .andExpect(jsonPath("$.totalElements").value(6))
            .andExpect(jsonPath("$.totalPages").value(2))
            .andExpect(jsonPath("$.size").value(3))
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].cards").isArray());
  }

  @Test
  void getAll_shouldReturnUsersWithCards_whenParameterName() throws Exception {
    User u1 = TestData.buildUserWithCards(1L);
    u1.setName("Ivan");
    User u2 = TestData.buildUserWithCards(2L);
    u2.setName("Ivan");
    userRepository.saveAll(List.of(u1, u2));
    TestAuthUtil.mockAuth(3L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(get("/api/users")
                    .param("name", "Ivan"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].name").value("Ivan"))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].cards").isArray());
  }

  @Test
  void getById_shouldReturnUserWithCards() throws Exception {
    User user = userRepository.save(TestData.buildUserWithCards(1L));
    Long id = user.getId();
    String name = user.getName();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(get("/api/users/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(name))
            .andExpect(jsonPath("$.cards").isArray())
            .andExpect(jsonPath("$.cards.length()").value(user.getCards().size()));
  }

  @Test
  void createUser_shouldCreateUser() throws Exception {
    UserCreateDto dto = TestData.buildUserCreateDto();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(dto.name()))
            .andExpect(jsonPath("$.email").value(dto.email()));
  }

  @Test
  void updateUser_shouldUpdateUser() throws Exception {
    User user = userRepository.save(TestData.buildUser(1L));
    Long id = user.getId();
    UserUpdateDto dto = TestData.buildUserUpdateDto();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(put("/api/users/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(dto.name()))
            .andExpect(jsonPath("$.surname").value(dto.surname()))
            .andExpect(jsonPath("$.email").value(dto.email()));
  }

  @Test
  void activateUser_shouldUpdateActiveToTrue() throws Exception {
    User newUser = TestData.buildUser(1L);
    newUser.setActive(false);
    User user = userRepository.save(newUser);
    Long id = user.getId();
    TestAuthUtil.mockAuth(2L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(patch("/api/users/" + id + "/activate"))
            .andExpect(status().isOk())
            .andExpect(content()
                    .string("Пользователь с id " + id + " активирован"));
  }

  @Test
  void deactivateUser_shouldUpdateActiveToFalse() throws Exception {
    User newUser = TestData.buildUser(1L);
    newUser.setActive(true);
    User user = userRepository.save(newUser);
    Long id = user.getId();
    TestAuthUtil.mockAuth(2L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(patch("/api/users/" + id + "/deactivate"))
            .andExpect(status().isOk())
            .andExpect(content()
                    .string("Пользователь с id " + id + " деактивирован"));
  }
}