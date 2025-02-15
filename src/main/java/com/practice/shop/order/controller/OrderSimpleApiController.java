package com.practice.shop.order.controller;

import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderRepository;
import com.practice.shop.order.domain.query.OrderSimpleQueryRepository;
import com.practice.shop.order.dto.SimpleOrderDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;
  private final OrderSimpleQueryRepository orderSimpleQueryRepository;

  @GetMapping("/api/v1/simple-orders")
  public List<Order> orderV1() {
    List<Order> orders = orderRepository.findAll();
    for (Order order : orders) {
      order.getMember().getName(); //Lazy 강제 초기화
      order.getDelivery().getAddress(); //Lazy 강제 초기화
    }
    return orders;
  }

  /**
   * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X) * - 단점: 지연로딩으로 쿼리 N번 호출
   */
  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> orderV2() {
    List<Order> orders = orderRepository.findAll();
    // N + 1 -> 1 + 회원 N + 배송 N
    return orders
        .stream()
        .map(SimpleOrderDto::new)
        .toList();
  }

  /**
   * fetch join
   * @return
   */
  @GetMapping("/api/v3/simple-orders")
  public List<SimpleOrderDto> orderV3() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    return orders
        .stream()
        .map(SimpleOrderDto::new)
        .toList();
  }

  @GetMapping("/api/v4/simple-orders")
  public List<SimpleOrderDto> orderV4() {
    return orderSimpleQueryRepository.findOrderDtos();

  }
}
