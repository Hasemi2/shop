package com.practice.shop.order.dto;

import com.practice.shop.member.domain.Address;
import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderItem;
import com.practice.shop.order.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

  private Long orderId;
  private String name;
  private LocalDateTime orderDate;
  private OrderStatus orderStatus;
  private Address address;
  private List<OrderItemDto> orderItems;

  public OrderDto(Order order) {
    this.orderId = order.getId();
    this.name = order.getMember().getName();
    this.orderDate = order.getOrderDate();
    this.orderStatus = order.getStatus();
    this.address = order.getDelivery().getAddress();
    this.orderItems = order.getOrderItems().stream().map(OrderItemDto::new).toList();
  }
}
