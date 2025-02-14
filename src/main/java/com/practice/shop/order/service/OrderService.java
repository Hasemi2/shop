package com.practice.shop.order.service;

import com.practice.shop.delivery.domain.Delivery;
import com.practice.shop.delivery.domain.DeliveryStatus;
import com.practice.shop.item.domain.Item;
import com.practice.shop.item.service.ItemService;
import com.practice.shop.member.domain.Member;
import com.practice.shop.member.service.MemberService;
import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderItem;
import com.practice.shop.order.domain.OrderRepository;
import com.practice.shop.order.domain.OrderSearch;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.hibernate.type.ListType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final MemberService memberService;
  private final OrderRepository orderRepository;
  private final ItemService itemService;

  @Transactional
  public Long order(Long memberId, Long itemId, int count) {
    Member member = memberService.findById(memberId);
    Item item = itemService.findById(itemId);

    Delivery delivery = new Delivery();
    delivery.setAddress(member.getAddress());
    delivery.setStatus(DeliveryStatus.READY);

    OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

    Order order = Order.createOrder(member, delivery, orderItem);

    return orderRepository.save(order).getId();
  }

  @Transactional
  public void cancelOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalStateException("존재하지 않는 주문입니다."));
    order.cancel();
  }

  // todo: querydsl 연동
  public List<Order> findOrders(OrderSearch orderSearch) {
    return null;
  }


}
