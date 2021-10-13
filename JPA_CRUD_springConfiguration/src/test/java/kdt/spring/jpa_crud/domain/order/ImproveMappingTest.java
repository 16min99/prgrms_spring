package kdt.spring.jpa_crud.domain.order;

import kdt.spring.jpa_crud.domain.parent.Parent;
import kdt.spring.jpa_crud.domain.parent.ParentE;
import kdt.spring.jpa_crud.domain.parent.ParentEId;
import kdt.spring.jpa_crud.domain.parent.ParentId;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class ImproveMappingTest {

    @Autowired
    private EntityManagerFactory emf;

    @Test
    void inheritance_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Food food = new Food();
        food.setPrice(1000);
        food.setStockQuantity(100);
        food.setChef("전민규");

        entityManager.persist(food);

        transaction.commit();
    }

    @Test
    void mapped_super_class_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderStatus(OrderStatus.OPENED);
        order.setMemo(" ---");
        order.setOrderDatetime(LocalDateTime.now());

        //
        order.setCreatedBy("minkyu");
        order.setCratedAt(LocalDateTime.now());

        entityManager.persist(order);

        transaction.commit();
    }

    @Test
    @DisplayName("복합키 예제 IdClass")
    void id_test1() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Parent parent = new Parent();
        parent.setId1("id1");
        parent.setId2("id2");

        entityManager.persist(parent);
        transaction.commit();

        entityManager.clear();
        Parent parent1 = entityManager.find(Parent.class, new ParentId("id1", "id2"));// 식별자 id값
        log.info("{} {}", parent1.getId1(), parent1.getId2());
    }

    @Test
    @DisplayName("복합키 예제 Embedded")
    void id_test2() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        ParentE parentE = new ParentE();
        parentE.setId(new ParentEId("id1", "id2"));


        entityManager.persist(parentE);
        transaction.commit();

        entityManager.clear();
        ParentE parentE1 = entityManager.find(ParentE.class, new ParentEId("id1", "id2"));// 식별자 id값
        log.info("{} {}", parentE1.getId().getId1(), parentE1.getId().getId2());
    }
}
