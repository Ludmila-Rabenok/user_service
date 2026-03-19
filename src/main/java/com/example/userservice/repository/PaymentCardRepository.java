package com.example.userservice.repository;

import com.example.userservice.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long>, JpaSpecificationExecutor<PaymentCard> {

  int countByUserId(Long userId);

  @Query(value = "SELECT * FROM payment_cards WHERE user_id = :userId", nativeQuery = true)
  List<PaymentCard> findCardsByUserId(@Param("userId") Long userId);

  @Modifying
  @Query("UPDATE PaymentCard c SET c.active = :active WHERE c.id = :id")
  int updateActiveStatus(@Param("id") Long id, @Param("active") boolean active);

  @Modifying
  @Query("UPDATE PaymentCard c SET c.active = :active WHERE c.user.id = :id")
  int updateActiveStatusByUserId(@Param("id") Long userId, @Param("active") boolean active);

  @Query("SELECT c.user.id FROM PaymentCard c WHERE c.id = :id")
  Long findUserIdByCardId(Long id);

}
