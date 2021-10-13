package com.kdt.lecture.order.service;

import com.kdt.lecture.domain.order.Order;
import com.kdt.lecture.domain.order.OrderRepository;
import com.kdt.lecture.order.converter.OrderConverter;
import com.kdt.lecture.order.dto.OrderDto;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private final OrderConverter orderConverter;

    @Autowired
    private final OrderRepository orderRepository;

    public String save (OrderDto orderDto) {
        Order order = orderConverter.convertOrder(orderDto); // 1. dto -> entity 변환 (준영속)
        Order entity = orderRepository.save(order);// 2. 영속화
        return entity.getUuid(); // 3. 결과 반환
    }

    public String update(String uuid, OrderDto orderDto) throws NotFoundException {
        Order order = orderRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));

        order.setMemo(orderDto.getMemo());
        order.setOrderStatus(order.getOrderStatus());

        return order.getUuid();
    }

    public Page<OrderDto> findOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderConverter::convertOrderDto);
    }

    public OrderDto findOne (String uuid) throws NotFoundException {
        // 1. 조회를 위한 키값을 인자로 받기
        return orderRepository.findById(uuid)
                .map(order -> orderConverter.convertOrderDto(order))
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));
        // 2. orderRepository.findById(uuid) -> 조회 (영속화된 엔티티)
        // 3. entity -> dto 로 변환 후 던져줌
    }
}
