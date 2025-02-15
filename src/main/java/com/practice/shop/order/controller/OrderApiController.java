package com.practice.shop.order.controller;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderItem;
import com.practice.shop.order.domain.OrderRepository;
import com.practice.shop.order.domain.query.OrderQueryRepository;
import com.practice.shop.order.dto.OrderDto;
import com.practice.shop.order.dto.OrderFlatDto;
import com.practice.shop.order.dto.OrderItemQueryDto;
import com.practice.shop.order.dto.OrderQueryDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  @GetMapping("/api/v1/orders")
  public List<Order> orderV1() {
    List<Order> orders = orderRepository.findAll();
    for (Order order : orders) {
      order.getMember().getName(); //proxy
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
        orderRepository.findAllWithMemberDeliveryWithPaging(
            PageRequest.of(offset, limit)); //toOne 관계는 fetch join
    return orders.stream()
        .map(OrderDto::new)
        .toList();

  }

  /**
   * row 수가 증가하지 않는 ToOne 관계는 조인으로 최적화 하기 쉬우므로 한번에 조회하고, ToMany 관계는 최적 화 하기 어려우므로 `findOrderItems()`
   * 같은 별도의 메서드로 조회
   *
   * @return
   */
  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> orderV4() {
    List<OrderQueryDto> orderQueryDtos =
        orderQueryRepository.findOrderQueryDtos(); //query 1번 -> N개

    //query n개 나감 결과적으론 n+1임
    orderQueryDtos.forEach(
        o -> o.setOrderItems(orderQueryRepository.findOrderItems(o.getOrderId())));

    return orderQueryDtos;
  }

  /**
   * Query: 루트 1번, 컬렉션 1번 ToOne 관계들을 먼저 조회하고, 여기서 얻은 식별자 orderId로 ToMany 관계인 `OrderItem` 을 한꺼번에 조회
   * map을 사용해서 매칭 성능 향상(O(1))
   *
   * @return
   */
  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> orderV5() {
    List<OrderQueryDto> orderQueryDtos =
        orderQueryRepository.findOrderQueryDtos();

    Map<Long, List<OrderItemQueryDto>> orderItemQueryaMap =
        orderQueryRepository.findOrderItemMap(orderQueryDtos.stream()
                .map(OrderQueryDto::getOrderId)
                .toList())
            .stream()
            .collect(groupingBy(OrderItemQueryDto::getOrderId));

    orderQueryDtos.forEach(o -> o.setOrderItems(orderItemQueryaMap.get(o.getOrderId())));

    return orderQueryDtos;

  }

  /**
   * Query: 1번
   * 쿼리는 한번이지만 조인으로 인해 DB에서 애플리케이션에 전달하는 데이터에 중복 데이터가 추가되므로 상황에 따라 V5 보다 더 느릴 수 도 있다.
   * 애플리케이션에서 추가 작업이 크다.
   * 페이징 불가능
   * @return
   */
  @GetMapping("/api/v6/orders")
  public List<OrderQueryDto> ordersV6() {
    List<OrderFlatDto> flats = orderQueryRepository.findOrderQueryDtosFlat();

    return flats.stream()
        .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(),
                o.getOrderStatus(), o.getAddress()),
            mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(),
                o.getCount()), toList())
        )).entrySet().stream()
        .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
            e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
            e.getKey().getAddress(), e.getValue()))
        .toList();


  }
}

