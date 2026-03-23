package com.example.userservice.integration;

import com.example.userservice.dto.paymentcard.PaymentCardCreateDto;
import com.example.userservice.dto.paymentcard.PaymentCardUpdateDto;
import com.example.userservice.dto.user.UserCreateDto;
import com.example.userservice.dto.user.UserUpdateDto;
import com.example.userservice.entity.PaymentCard;
import com.example.userservice.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TestData {

  private static final Random RANDOM = new Random();


  public static User buildUser() {
    User user = new User();
    user.setName("Ivan");
    user.setSurname("ivanov");
    user.setEmail("user_" + UUID.randomUUID().toString().substring(0, 8) + "@mail.com");
    user.setActive(true);
    user.setBirthDate(LocalDate.of(1990, 1, 1));
    user.setCards(new ArrayList<>());
    return user;
  }

  public static List<User> buildUsers(int count) {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      users.add(buildUser());
    }
    return users;
  }

  public static List<User> buildUsersWithCards(int count) {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      users.add(buildUserWithCards());
    }
    return users;
  }

  public static User buildUserWithCards() {
    User user = buildUser();
    List<PaymentCard> cards = buildCards(2);
    for (PaymentCard card : cards) {
      card.setUser(user);
    }
    user.setCards(cards);
    return user;
  }

  public static PaymentCard buildCard() {
    PaymentCard card = new PaymentCard();
    card.setNumber(randomCardNumber());
    card.setHolder("Ivan Ivanov");
    card.setExpirationDate(LocalDate.now().plusYears(2));
    card.setActive(true);
    return card;
  }

  public static List<PaymentCard> buildCards(int count) {
    List<PaymentCard> cards = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      cards.add(buildCard());
    }
    return cards;
  }

  public static UserCreateDto buildUserCreateDto() {
    return new UserCreateDto(
            "Ivan",
            "Ivanov",
            LocalDate.of(1990, 1, 1),
            "ivan@mail.com"
    );
  }

  public static UserUpdateDto buildUserUpdateDto() {
    return new UserUpdateDto(
            "Ivan",
            "Ivanov",
            LocalDate.of(1990, 1, 1),
            "ivan@mail.com",
            true
    );
  }

  public static PaymentCardCreateDto buildCardCreateDto(Long userId) {
    return new PaymentCardCreateDto(
            userId,
            "1111222233334444",
            "Ivan Ivanov",
            LocalDate.now().plusYears(2)
    );
  }

  public static PaymentCardUpdateDto buildCardUpdateDto() {
    return new PaymentCardUpdateDto(
            "1111222233334444",
            "Ivan Ivanov",
            LocalDate.now().plusYears(2),
            true
    );
  }

  private static String randomCardNumber() {
    return "1111222233334444" + (1000 + RANDOM.nextInt(9000));
  }
}