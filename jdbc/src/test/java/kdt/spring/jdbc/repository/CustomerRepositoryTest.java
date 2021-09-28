package kdt.spring.jdbc.repository;

import static org.junit.jupiter.api.Assertions.*;

import kdt.spring.jdbc.repository.domain.Customer;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("TRUNCATE TABLE customers");
    }


    @Test
    void 고객정보가_저장되는지_확인한다() {
        // Given
        Customer customer = new Customer(1L, "honggu", "kang");

        // When
        customerRepository.save(customer);

        // Then
        Long allCount = jdbcTemplate.queryForObject("SELECT count(*) FROM customers", Long.class);
        assertThat(allCount).isEqualTo(1L);
    }

    @Test
    void 고객정보가_수정되는지_확인한다() {
        // Given
        Customer customer = new Customer(1L, "honggu", "kang");
        customerRepository.save(customer);
        customer.setFirstName("guppy");
        customer.setLastName("hong");

        // When
        customerRepository.update(customer);

        // Then
        Customer updated = jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE id = ?",
                (resultSet, rowNum) -> new Customer(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name")),
                customer.getId()
        );
        assertThat(updated.getLastName()).isEqualTo(customer.getLastName());
        assertThat(updated.getFirstName()).isEqualTo(customer.getFirstName());
    }

    @Test
    void 단건조회를_확인한다() {
        // Given
        Customer customer = new Customer(1L, "honggu", "kang");
        customerRepository.save(customer);

        // When
        Customer selected = customerRepository.findById(customer.getId());

        // Then
        assertThat(customer.getId()).isEqualTo(selected.getId());
    }

    @Test
    void 리스트조회를_확인한다() {
        // Given
        List<Customer> customers = Lists.newArrayList(
                new Customer(1L, "honggu", "kang"),
                new Customer(2L, "guppy", "hong")
        );

        jdbcTemplate.batchUpdate(
                "INSERT INTO customers (id, first_name, last_name) VALUES(?, ?, ?)",
                customers,
                customers.size(),
                (ps, arg) -> {
                    ps.setLong(1, arg.getId());
                    ps.setString(2, arg.getFirstName());
                    ps.setString(3, arg.getLastName());
                }
        );
        // When
        List<Customer> selectedCustomers = customerRepository.findAll();

        // Then
        assertThat(selectedCustomers.size()).isEqualTo(customers.size());
    }
}
