package kdt.spring.jpa_crud.repository;

import kdt.spring.jpa_crud.repository.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("save와 findById 테스트")
    void testInsertFindById() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");

        // When
        repository.save(customer);

        //Then
        Customer entity = repository.findById(1L).get();
        assertThat(entity.getId()).isEqualTo(customer.getId());
        assertThat(entity.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(customer.getLastName());
    }

    @Test
    @DisplayName("Update 테스트")
    @Transactional
    void testUpdate() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");
        repository.save(customer);

        // When
        Customer entity = repository.findById(1L).get();
        entity.setFirstName("guppy");
        entity.setLastName("hong");

        // Then
        Customer updated = repository.findById(1L).get();
        assertThat(updated.getId()).isEqualTo(entity.getId());
        assertThat(updated.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(updated.getLastName()).isEqualTo(entity.getLastName());
    }

    @Test
    @DisplayName("delete 테스트")
    void testDelete() {
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("minkyu");
        customer.setLastName("jeon");
        repository.save(customer);

        // When
        repository.deleteById(1L);

        // Then
        List<Customer> customerList = repository.findAll();
        assertThat(customerList.size()).isEqualTo(0);
    }
}