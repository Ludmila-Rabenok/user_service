package com.example.userservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends Auditable {

  @Id
  private Long id;
  private String name;
  private String surname;
  private LocalDate birthDate;
  private String email;
  private boolean active;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PaymentCard> cards = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<PaymentCard> getCards() {
    return cards;
  }

  public void setCards(List<PaymentCard> cards) {
    this.cards = cards;
  }

  public void addCard(PaymentCard card) {
    if (cards.size() >= 5) {
      throw new IllegalStateException("User already has 5 cards");
    }
    cards.add(card);
    card.setUser(this);
  }

  public void removeCard(PaymentCard card) {
    cards.remove(card);
    card.setUser(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append(", birthDate=").append(birthDate);
    sb.append(", email='").append(email).append('\'');
    sb.append(", active=").append(active);
    sb.append(", cardsCount=").append(cards != null ? cards.size() : 0);
    sb.append('}');
    return sb.toString();
  }
}
