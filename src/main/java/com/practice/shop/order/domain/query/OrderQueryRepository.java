package com.practice.shop.order.domain.query;

import com.practice.shop.order.domain.Order;
import com.practice.shop.order.dto.OrderFlatDto;
import com.practice.shop.order.dto.OrderItemQueryDto;
import com.practice.shop.order.dto.OrderQueryDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderQueryRepository extends JpaRepository<Order, Long> {

  @Query(
      "select new com.practice.shop.order.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o "
          + " join o.member m "
          + " join o.delivery d "
  )
  List<OrderQueryDto> findOrderQueryDtos();

  @Query(
      "select new com.practice.shop.order.dto.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count  ) from OrderItem oi "
          + " join oi.item i "
          + " where oi.order.id = :orderId")
  List<OrderItemQueryDto> findOrderItems(Long orderId);


  @Query(
      "select new com.practice.shop.order.dto.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count  ) from OrderItem oi "
          + " join oi.item i "
          + " where oi.order.id in :orderIds")
  List<OrderItemQueryDto> findOrderItemMap(List<Long> orderIds);

  @Query(
      "select new com.practice.shop.order.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)"
          + " from Order o "
          + " join o.member m "
          + " join o.delivery d "
          + " join o.orderItems oi "
          + " join oi.item i "
  )
  List<OrderFlatDto> findOrderQueryDtosFlat();
}
