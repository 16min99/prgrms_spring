package com.kdt.lecture.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);
    List<Order> findAllByOrderStatusOrderByOrderDatetime(OrderStatus orderStatus);

    @Query("SELECT o FROM Order AS o WHERE o.memo LIKE %?1%") //@query 기반
    Optional<Order> findByMemo(String memo);
}
