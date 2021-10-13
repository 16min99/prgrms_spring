package kdt.spring.jpa_crud.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

import static kdt.spring.jpa_crud.domain.order.OrderStatus.OPENED;

@Slf4j
@SpringBootTest
public class OrderPersistenceTest {

    @Autowired
    EntityManagerFactory emf;

    @Test
    void test_member_insert() {
        Member member = new Member();
        member.setName("JeonMinkyu");
        member.setAddress("인천시 미추홀구 용현동");
        member.setAge(26);
        member.setNickName("만두");
        member.setDescription("백엔드 개발자에요");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(member);
        transaction.commit();
    }

    @Test
    void 잘못된_설계() {
        Member member = new Member();
        member.setName("kanghonggu");
        member.setAddress("서울시 동작구(만) 움직이면 쏜다.");
        member.setAge(33);
        member.setNickName("guppy.kang");
        member.setDescription("백앤드 개발자에요.");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(member); // 저장
        Member memberEntity = entityManager.find(Member.class, 1L); // 영속화된 회원

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);
        order.setMemo("부재시 전화주세요.");
        order.setMemberId(memberEntity.getId()); // 외래키를 직접 지정

        entityManager.persist(order);
        transaction.commit();

        // -> 회원 , 주문 저장

        Order orderEntity = entityManager.find(Order.class, order.getUuid()); // select Order
        // FK 를 이용해 회원 다시 조회
        Member orderMemberEntity = entityManager.find(Member.class, orderEntity.getMemberId()); // 또다시 select member
        // orderEntity.getMember() // 객체중심 설계라면 객체그래프 탐색을 해야하지 않을까? -> 이렇게 연관관계맵핑을 통해서 가져올수 있어야함
        log.info("nick : {}", orderMemberEntity.getNickName());

        // 즉, 주문을 가져왔는데 주문에 딸린 회원을 가져오기 위해서  다시한번 엔티티 매니저를 통해서 조회를 통해 회원을 가져와야 한다.
        // ERD 적인 프로그래밍임
        // 이것을 객체 중심 프로그래밍을 위해서  연관관계 맵핑을 해야함!
    }

    @Test // 오더에서 멤버 단방향
    void 연관관계_테스트(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Member member = new Member();
        member.setName("Jonminkyu");
        member.setNickName("min.kyu");
        member.setAddress("인천시 미추홀구 용현동");
        member.setAge(26);

        entityManager.persist(member);

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderStatus(OPENED);
        order.setOrderDatetime(LocalDateTime.now());
        order.setMemo("부재시 연락주세요");
        order.setMemberG(member);
        //member.setOrders(Lists.newArrayList(order)); // 여기 연관관계 편의 메소드를 만들어서 방지해줬음.

        entityManager.persist(order);
        transaction.commit();
        log.info("{}",order.getMemberG().getNickName()); //객체 그래프 탐색

        entityManager.clear();
        Order entity = entityManager.find(Order.class,order.getUuid());

        log.info("{}",entity.getMemberG().getNickName()); //객체 그래프 탐색
        log.info("{}",entity.getMemberG().getOrders().size());// order->member->order로 확인 (양방향)

        //단방향 문제점 (오더->멤버 set 단방향)
        log.info("{}", order.getMemberG().getOrders().size()); // 하면 0나옴
        //101번째 줄처럼 항상 member에도 setOrders를 해줘야함
        //이과정 편하게 하기위해서 setMemberG할때 setOrders가 되게해야함
    }

    @Test
    void 연관관계_테스트2_orderItem(){ // ??? 왜안댐 ㅠ
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Member member = new Member();
        member.setName("Jonminkyu");
        member.setNickName("min.kyu");
        member.setAddress("인천시 미추홀구 용현동");
        member.setAge(26);
        entityManager.persist(member); //영속성 컨텍스트에 등록

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderStatus(OPENED);
        order.setOrderDatetime(LocalDateTime.now());
        order.setMemo("부재시 연락주세요");
        //order.setMemberG(member);
        member.addOrder(order);
        entityManager.persist(order);


        Food food = new Food();
        food.setPrice(1000);
        food.setStockQuantity(100);
        food.setChef("전민규");
        entityManager.persist(food);

        Item item = entityManager.find(Item.class,food.getId());

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setPrice(5000);
        orderItem.setQuantity(5);
        orderItem.setItem(item);
        item.addOrderItem(orderItem);
        order.addOrderItem(orderItem);
        entityManager.persist(orderItem);


        transaction.commit();
        OrderItem orderItemEntity = entityManager.find(OrderItem.class, orderItem.getId());
        log.info("Order :{}", orderItemEntity.getOrder());

    }

}

//create table member
//        (id bigint not null,
//         address varchar(255) not null,
//        age integer not null,
//        description varchar(255),
//        name varchar(30) not null,
//        nick_name varchar(30) not null,
//        primary key (id))
