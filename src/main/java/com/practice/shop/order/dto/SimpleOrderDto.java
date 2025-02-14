package com.practice.shop.order.dto;

import com.practice.shop.member.domain.Address;
import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SimpleOrderDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;

  public SimpleOrderDto(Order order) {
    this.orderId = order.getId();
    this.name = order.getMember().getName();
    this.orderDate = order.getOrderDate();
    this.orderStatus = order.getStatus();
    this.address = order.getDelivery().getAddress();
  }
}
