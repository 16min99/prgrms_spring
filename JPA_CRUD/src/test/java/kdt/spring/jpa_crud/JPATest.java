package kdt.spring.jpa_crud;

import kdt.spring.jpa_crud.repository.CustomerRepository;
import kdt.spring.jpa_crud.repository.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class JPATest {

    @Autowired
    CustomerRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void INSERT_TEST() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");

        // When
        repository.save(customer);

        //Then
        Customer entity = repository.findById(1L).get();
        log.info("{} {}", entity.getFirstName(), entity.getLastName());
    }

    @Test
    @Transactional //영속성 컨텍스트에서 관리하겠다. 더티 체킹을 통해서 테스트가 통과될 때 컨텍스트에서 관리된 객체를 DB에 올리는 커밋이 이루어진다.
    void UPDATE_TEST(){
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");
        repository.save(customer);

        // When
        Customer entity = repository.findById(1L).get();
        //entity 객체는 영속성 컨텍스트에서 관리가 된다.
        entity.setFirstName("guppy");
        entity.setLastName("hong");
        // 컨텍스트에서 엔티티의 변경사항을 감지하고 트랜잭션 커밋될때 DB에 적용함(쿼리가 날라감)
        // 따라서 쿼리를 호출하지 않아도 ( 단순 객체 수정을 통해서) DB 수정이 가능해짐 !
        // 심지어 테이블의 속성을 추가할 수도 있음! 엔티티에서 멤버변수를 추가해주면됨!

        Customer updated = repository.findById(1L).get();
        log.info("{} {}", updated.getFirstName(), updated.getLastName());
    }

    @Test
    void 고객정보가_저장되는지_확인한다() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");

        // When
        repository.save(customer); // INSERT INTO ..

        // Then
        Customer selectedEntity = repository.findById(1L).get(); // SELECT * FROM ..
        assertThat(selectedEntity.getId()).isEqualTo(1L);
        assertThat(selectedEntity.getFirstName()).isEqualTo(customer.getFirstName());
    }

    @Test
    void 단건조회를_확인한다() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");
        repository.save(customer);

        // When
        Customer selected = repository.findById(customer.getId()).get();

        // Then
        assertThat(customer.getId()).isEqualTo(selected.getId());
    }

    @Test
    void 리스트조회를_확인한다() {
        // Given
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("honggu");
        customer1.setLastName("kang");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("guppy");
        customer2.setLastName("hong");

        repository.saveAll(Lists.newArrayList(customer1, customer2));

        // When
        List<Customer> selectedCustomers = repository.findAll();

        // Then
        assertThat(selectedCustomers.size()).isEqualTo(2);
    }
}
