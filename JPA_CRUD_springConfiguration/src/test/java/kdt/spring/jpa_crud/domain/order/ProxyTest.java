package kdt.spring.jpa_crud.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
public class ProxyTest {
    @Autowired
    EntityManagerFactory emf;

    private String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // 주문 엔티티
        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setMemo("부재시 전화주세요.");
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);

        entityManager.persist(order);

        // 회원 엔티티
        Member member = new Member();
        member.setName("jeonminkyu");
        member.setNickName("guppy.jeon");
        member.setAge(33);
        member.setAddress("인천시 용현동");
        member.setDescription("전민규 파이팅!!");

        member.addOrder(order); // 연관관계 편의 메소드 사용
        entityManager.persist(member);
        transaction.commit();
    }

    @Test
    void proxy() {
        EntityManager entityManager = emf.createEntityManager();
        Order order = entityManager.find(Order.class, uuid);

        Member member = order.getMemberG(); // member는 프록시객체로 비어있음 (FAZY)
        log.info("MEMBER USE BEFORE IS_LOADED: {}", emf.getPersistenceUnitUtil().isLoaded(member));
        String nickName = member.getNickName(); // member를 사용하게되면 쿼리를 날려 사용하게됨
        log.info("MEMBER USE AFTER IS-LOADED: {}", emf.getPersistenceUnitUtil().isLoaded(member));

        // 회원 조회 -> 회원의 주문 까지 조회
        Member findMember = entityManager.find(Member.class, 1L);

        log.info("orders is loaded : {}", entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(findMember.getOrders()));
        log.info("-------");
        log.info("{}", findMember.getOrders().get(0).getMemo());
        log.info("orders is loaded : {}", entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(findMember.getOrders()));
    }

    @Test
    void move_persist() {
        EntityManager entityManager = emf.createEntityManager();
        Order order = entityManager.find(Order.class, uuid);

        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        OrderItem item = new OrderItem(); // 준영속상태
        item.setQuantity(10);
        item.setPrice(1000);

        order.addOrderItem(item);

        transaction.commit(); //flush()
        entityManager.clear();
        ////////

        Order order2 = entityManager.find(Order.class, uuid);

        transaction.begin();

        order2.getOrderItems().remove(0); // 고아상태

        transaction.commit(); // flush 순간 orphanRemoval = true일때
        // RDS에서도 삭제를 하겠다.
    }

    @Test
    void orphan() {
        EntityManager entityManager = emf.createEntityManager();

        // 회원 조회 -> 회원의 주문 까지 조회
        Member findMember = entityManager.find(Member.class, 1L);
        findMember.getOrders().remove(0);

        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        transaction.commit();
    }
}
