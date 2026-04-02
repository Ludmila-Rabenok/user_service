package com.example.userservice.integration.controller;

import com.example.userservice.TestAuthUtil;
import com.example.userservice.dto.paymentcard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentcard.PaymentCardUpdateDto;
import com.example.userservice.entity.PaymentCard;
import com.example.userservice.entity.User;
import com.example.userservice.integration.AbstractIntegrationTest;
import com.example.userservice.integration.TestData;
import com.example.userservice.repository.PaymentCardRepository;
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
class PaymentCardControllerIT extends AbstractIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PaymentCardRepository cardRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setup() {
    cardRepository.deleteAll();
    userRepository.deleteAll();
  }

  @AfterEach
  void clearContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void getAll_shouldReturnAllCards_whenNoParameters() throws Exception {
    User user = TestData.buildUserWithCards(1L);
    int size = user.getCards().size();
    userRepository.save(user);
    TestAuthUtil.mockAuth(2L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(get("/api/cards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(size))
            .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void getAll_shouldReturnActiveCards_whenActiveTrue() throws Exception {
    User user = TestData.buildUserWithCards(1L);
    List<PaymentCard> cards = user.getCards();
    int size = cards.size();
    cards.get(0).setActive(true);
    cards.get(1).setActive(true);
    userRepository.save(user);
    TestAuthUtil.mockAuth(4L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(get("/api/cards")
                    .param("active", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(size))
            .andExpect(jsonPath("$.content[0].active").value(true))
            .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void getAll_shouldReturnPagedCards_whenPageAndSize() throws Exception {
    List<User> users = TestData.buildUsersWithCards(3);
    int allSize = 0;
    for (User user : users) {
      List<PaymentCard> cards = user.getCards();
      allSize += cards.size();
    }
    userRepository.saveAll(users);
    TestAuthUtil.mockAuth(4L, TestAuthUtil.ROLE_ADMIN);
    mockMvc.perform(get("/api/cards")
                    .param("page", "0")
                    .param("size", "3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(3))
            .andExpect(jsonPath("$.totalElements").value(allSize))
            .andExpect(jsonPath("$.totalPages").value(allSize / 3))
            .andExpect(jsonPath("$.size").value(3))
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void getById_shouldReturnCard() throws Exception {
    User user = userRepository.save(TestData.buildUserWithCards(1L));
    PaymentCard card = user.getCards().get(0);
    Long id = card.getId();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(get("/api/cards/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.number").value(card.getNumber()));
  }

  @Test
  void getUserCards_shouldReturnCardsOfUser() throws Exception {
    User user = userRepository.save(TestData.buildUserWithCards(1L));
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(get("/api/cards/user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].userId").value(user.getId()))
            .andExpect(jsonPath("$[1].userId").value(user.getId()));
  }

  @Test
  void createCard_shouldCreateCard() throws Exception {
    User user = userRepository.save(TestData.buildUser(1L));
    PaymentCardCreateDto dto = TestData.buildCardCreateDto();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(post("/api/cards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.number").value(dto.number()))
            .andExpect(jsonPath("$.userId").value(user.getId()));
  }

  @Test
  void updateCard_shouldUpdateCard() throws Exception {
    User user = userRepository.save(TestData.buildUserWithCards(1L));
    PaymentCard card = user.getCards().get(0);
    Long id = card.getId();
    PaymentCardUpdateDto dto = TestData.buildCardUpdateDto();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(put("/api/cards/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.number").value(dto.number()));
  }

  @Test
  void activateCard_shouldSetActiveTrue() throws Exception {
    User newUser = TestData.buildUserWithCards(1L);
    newUser.setActive(true);
    PaymentCard card = newUser.getCards().get(0);
    card.setActive(false);
    User user = userRepository.save(newUser);
    Long id = user.getCards().get(0).getId();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(patch("/api/cards/" + id + "/activate"))
            .andExpect(status().isOk())
            .andExpect(content()
                    .string("Карта пользователя с id " + user.getId() + " активирована"));
  }

  @Test
  void deactivateCard_shouldSetActiveFalse() throws Exception {
    User newUser = TestData.buildUserWithCards(1L);
    PaymentCard card = newUser.getCards().get(0);
    card.setActive(true);
    User user = userRepository.save(newUser);
    Long id = user.getCards().get(0).getId();
    TestAuthUtil.mockAuth(1L, TestAuthUtil.ROLE_USER);
    mockMvc.perform(patch("/api/cards/" + id + "/deactivate"))
            .andExpect(status().isOk())
            .andExpect(content()
                    .string("Карта пользователя с id " + user.getId() + " деактивирована"));
  }
}