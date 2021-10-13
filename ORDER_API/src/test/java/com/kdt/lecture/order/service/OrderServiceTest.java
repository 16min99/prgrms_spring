package com.kdt.lecture.order.service;

import com.kdt.lecture.domain.order.Order;
import com.kdt.lecture.domain.order.OrderRepository;
import com.kdt.lecture.domain.order.OrderStatus;
import com.kdt.lecture.item.dto.ItemDto;
import com.kdt.lecture.item.dto.ItemType;
import com.kdt.lecture.member.dto.MemberDto;
import com.kdt.lecture.order.dto.OrderDto;
import com.kdt.lecture.order.dto.OrderItemDto;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp () {
        // Given
        OrderDto orderDto = OrderDto.builder()
                .uuid(uuid)
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(
                        MemberDto.builder()
                                .name("전민규")
                                .nickName("minkyu.jeon")
                                .address("인천시 미추홀구")
                                .age(26)
                                .description("---")
                                .build()
                )
                .orderItemDtos(List.of(
                        OrderItemDto.builder()
                                .price(1000)
                                .quantity(100)
                                .itemDto(
                                        ItemDto.builder()
                                                .type(ItemType.FOOD)
                                                .chef("백종원")
                                                .price(1000)
                                                .build()
                                )
                                .build()
                ))
                .build();
        // When
        String saveUuid = orderService.save(orderDto);

        // Then
        assertThat(uuid).isEqualTo(saveUuid);
        log.info("UUID:{}", uuid);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void testFindAll() {
        // given
        PageRequest page = PageRequest.of(0,10);
        // when
        Page<OrderDto> orders = orderService.findOrders(PageRequest.of(0, 10));
        // then
        log.info("{}", orders);
        assertThat(orders.getTotalElements()).isEqualTo(1);

    }

    @Test
    void testFindOne() throws NotFoundException {
        log.info("uuid:{}", uuid);
        OrderDto one = orderService.findOne(uuid);
        log.info("{}", one);
    }

}