package kdt.spring.jpa_crud.repository;

import kdt.spring.jpa_crud.repository.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Slf4j
@SpringBootTest
public class PersistenceContextTest {

    @Autowired
    CustomerRepository repository;

    @Autowired
    EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("저장 테스트")
    void testSave() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin(); //트랜잭션 획득

        Customer customer = new Customer(); // 비영속상태
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); // entityManager.flush();
        // 트랜잭션이 커밋이 되는 순간 쿼리가 수행된다. flush DB와 동기화가 된다.
    }

    @Test
    @DisplayName("DB에서조회 테스트")
    void testReadDB() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(); // 비영속상태
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); // entityManager.flush();

        entityManager.detach(customer); // 영속 -> 준영속

        Customer selected = entityManager.find(Customer.class, 1L);
        // 영속성 컨텍스트에 엔티티가 없으므로 DB 에서 조회한다. SELECT ...
        // DB에서 조회시 자동으로 영속성 컨텍스트에 등록됨!!
        log.info("{} {}", selected.getFirstName(), selected.getLastName());
    }

    @Test
    @DisplayName("1차 캐시를 이용해서 조회 테스트")
    void testReadCash() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(); // 비영속상태
        customer.setId(1L);
        customer.setFirstName("honggu");
        customer.setLastName("kang");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); // entityManager.flush();

        Customer selected = entityManager.find(Customer.class, 1L); // 엔티티의 1차 캐시에서 조회한다.
        // 위의 경우 디비에 쿼리가 날라가지 않음 (굳이 DB까지 가지않음)
        log.info("{} {}", selected.getFirstName(), selected.getLastName());
    }

    @Test
    @DisplayName("업데이트 테스트")
    void testUpdate() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(); // 비영속상태
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); // entityManager.flush();
        // 엔티티를 영속화한후, 커밋을해서 flush()를 통해 DB에 저장.

        transaction.begin();
        customer.setFirstName("guppy");
        customer.setLastName("hong");

        // em.update(entity) ??!! 안해도됨!! 더티 체킹(변경 감지)을 하기때문 , 영속 상태 엔티티만 업데이트함
        transaction.commit();
    }

    @Test
    @DisplayName("삭제 테스트")
    void testDelete() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(); // 비영속상태
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); // entityManager.flush();

        transaction.begin();

        entityManager.remove(customer);

        transaction.commit();
    }

}
