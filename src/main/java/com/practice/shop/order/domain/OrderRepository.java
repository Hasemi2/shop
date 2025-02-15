package com.practice.shop.order.domain;

import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query("select o from Order o join fetch o.member m join fetch o.delivery d")
  List<Order> findAllWithMemberDelivery();

  @Query("select o from Order o "
      + " join fetch o.member m "
      + " join fetch o.delivery d "
      + " join fetch o.orderItems oi "
      + " join fetch oi.item i")
  List<Order> findAllWithItem();

  @Query("select o from Order o join fetch o.member m join fetch o.delivery d ")
  List<Order> findAllWithMemberDeliveryWithPaging(Pageable pageable);
}
