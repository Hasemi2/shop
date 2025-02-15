package com.practice.shop.order.domain.query;

import com.practice.shop.order.domain.Order;
import com.practice.shop.order.dto.SimpleOrderDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderSimpleQueryRepository extends JpaRepository<Order, Long> {

  @Query("select new com.practice.shop.order.dto.SimpleOrderDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o join  o.member m join o.delivery d")
  List<SimpleOrderDto> findOrderDtos();
}
