package com.practice.shop.order.service;

import com.practice.shop.delivery.domain.Delivery;
import com.practice.shop.delivery.domain.DeliveryStatus;
import com.practice.shop.item.domain.Item;
import com.practice.shop.item.service.ItemService;
import com.practice.shop.member.domain.Member;
import com.practice.shop.member.domain.QMember;
import com.practice.shop.member.service.MemberService;
import com.practice.shop.order.domain.Order;
import com.practice.shop.order.domain.OrderItem;
import com.practice.shop.order.domain.OrderRepository;
import com.practice.shop.order.domain.OrderSearch;
import com.practice.shop.order.domain.OrderStatus;
import com.practice.shop.order.domain.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final MemberService memberService;
  private final OrderRepository orderRepository;
  private final ItemService itemService;
  private final EntityManager em;

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

  public List<Order> findOrders(OrderSearch orderSearch) {
    QOrder order = QOrder.order;
    QMember member = QMember.member;

    JPAQueryFactory query = new JPAQueryFactory(em);

    return query
        .select(order)
        .from(order)
        .join(order.member, member)
        .where(statusEq(orderSearch.getOrderStatus()), memberNameLike(orderSearch.getMemberName()))
        .limit(1000)
        .fetch();

  }

  private BooleanExpression statusEq(OrderStatus orderStatus) {
    if (Objects.isNull(orderStatus)) {
      return null;
    }
    return QOrder.order.status.eq(orderStatus);
  }

  private BooleanExpression memberNameLike(String name) {
    if (StringUtils.hasText(name)) {
      return null;
    }
    return QMember.member.name.like(name);
  }

}
