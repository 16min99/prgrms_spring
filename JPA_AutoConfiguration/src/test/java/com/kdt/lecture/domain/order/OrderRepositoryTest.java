package com.kdt.lecture.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class OrderRepositoryTest {

    String uuid = UUID.randomUUID().toString();

    @Autowired // 엔티티 매니저 팩토리가아니라 오더레포지토리를 오토와이어드를 통해서 !
    OrderRepository orderRepository;

    @BeforeEach
    void setUp () {
        Member member = new Member();
        member.setName("전민규");
        member.setAge(27);
        member.setNickName("minkyu.jeon");
        member.setAddress("인천시 용현동만 움직이면 쏜다.");
        member.setDescription("");

        Order order = new Order();
        order.setUuid(uuid);
        order.setMemo("부재시 전화주세요.");
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.OPENED);

        order.setMember(member);

        orderRepository.save(order); // INSERT
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }


    @Test
    void test(){
        String uuid1 = UUID.randomUUID().toString();
        Order order = new Order();
        order.setUuid(uuid1);
        order.setMemo("부재시 전화주세요.");
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.OPENED);
        order.setCreatedBy("minkyu.jeon");
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        Order order1 = orderRepository.findById(uuid1).get();
        List<Order> all = orderRepository.findAll();
    }

    @Test
    @Transactional
    void JPA_query() {
        Order order = orderRepository.findById(uuid).get(); // SELECT * FROM orders WHERE id = ?
        List<Order> all = orderRepository.findAll(); // SELECT * FROM orders
        orderRepository.existsById(uuid);
    }

    @Test
    @Transactional
    void METHOD_QUERY() {
        orderRepository.findAllByOrderStatus(OrderStatus.OPENED);
        orderRepository.findAllByOrderStatusOrderByOrderDatetime(OrderStatus.OPENED);
    }


    @Test
    void NAMED_QUERY() {
        Optional<Order> order = orderRepository.findByMemo("부재시");

        Order entity = order.get();
        log.info("{}", entity.getMemo());
    }
}
