package com.practice.shop.order.controller;

import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderSearch;
import com.practice.shop.order.domain.OrderStatus;
import com.practice.shop.order.dto.OrderDto;
import com.practice.shop.order.dto.OrderQueryDto;
import com.practice.shop.order.service.OrderQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {


  private final OrderQueryService orderQueryService;

  @GetMapping("/api/v1/orders")
  public List<Order> orderV1() {
    return orderQueryService.ordersV1();
  }

  @GetMapping("/api/v2/orders")
  public List<OrderDto> orderV2() {
    return orderQueryService.orderV2();

  }

  @GetMapping("/api/v3/orders")
  public List<OrderDto> orderV3() {
    return orderQueryService.orderV3();
  }

  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> orderV3withPaging(
      @RequestParam(defaultValue = "0") int offset,
      @RequestParam(defaultValue = "100") int limit
  ) {
    return orderQueryService.orderV3withPaging(offset, limit);
  }

  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> orderV4() {
    return orderQueryService.ordersV4();
  }


  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> orderV5() {
    return orderQueryService.ordersV5();
  }


  @GetMapping("/api/v6/orders")
  public List<OrderQueryDto> ordersV6() {
    return orderQueryService.ordersV6();
  }

  @GetMapping("/api/orders/search")
  public List<OrderQueryDto> ordersSearch(@RequestParam(required = false) String memberName,
      @RequestParam(required = false) OrderStatus orderStatus) {
    OrderSearch orderSearch = new OrderSearch();
    orderSearch.setOrderStatus(orderStatus);
    orderSearch.setMemberName(memberName);
    return orderQueryService.findOrders(orderSearch);
  }
}



