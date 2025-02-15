package com.practice.shop.order.controller;

import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderItem;
import com.practice.shop.order.domain.OrderRepository;
import com.practice.shop.order.dto.OrderDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;


  @GetMapping("/api/v1/orders")
  public List<Order> orderV1() {
    List<Order> orders = orderRepository.findAll();
    for (Order order : orders) {
      order.getMember().getName();
      order.getDelivery().getAddress();
      List<OrderItem> orderItems = order.getOrderItems();
      orderItems.forEach(o -> o.getItem().getName());
    }

    return orders;
  }

  @GetMapping("/api/v2/orders")
  public List<OrderDto> orderV2() {
    return orderRepository.findAll().stream()
        .map(OrderDto::new)
        .toList();

  }

  /**
   * hibernate6 이후 자동 distinct 처리가 됨 컬렉션 페치 조인은 페이징이 안됨 ( 모든 데이터 select 후 메모리에서 페이징)
   *
   * @return
   */
  @GetMapping("/api/v3/orders")
  public List<OrderDto> orderV3() {
    return orderRepository.findAllWithItem().stream()
        .map(OrderDto::new)
        .toList();

  }

  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> orderV3withPaging(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "100") int limit
  ) {
      List<Order> orders =
          orderRepository.findAllWithMemberDeliveryWithPaging(PageRequest.of(offset, limit)); //toOne 관계는 fetch join
    return orders.stream()
        .map(OrderDto::new)
        .toList();

  }



}

