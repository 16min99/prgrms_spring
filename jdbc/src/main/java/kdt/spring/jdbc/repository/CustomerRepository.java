package kdt.spring.jdbc.repository;

import kdt.spring.jdbc.repository.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomerRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public long save(Customer customer) {
        int insertCount = jdbcTemplate.update(
                "INSERT INTO customers (id, first_name, last_name) VALUES(?, ?, ?)",
                customer.getId(), customer.getFirstName(), customer.getLastName()
        );
        log.info("고객정보 {}건이 입력되었습니다.", insertCount);

        return customer.getId();
    }

    public long update(Customer customer) {
        int updateCount = jdbcTemplate.update(
                "UPDATE customers SET first_name = ? , last_name = ? WHERE id = ?",
                customer.getFirstName(), customer.getLastName(), customer.getId()
        );
        log.info("고객정보 {}건이 수정되었습니다.", updateCount);
        return customer.getId();
    }

    public Customer findById(long id) {
        Customer customer = jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE id = ?",
                (resultSet, rowNum) -> new Customer(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name")),
                id
        );
        log.info("아이다:{} 고객의 정보가 조회되었습니다." ,customer.getId());

        return customer;
    }

    public List<Customer> findAll() {
        List<Customer> customers = jdbcTemplate.query(
                "SELECT * FROM customers",
                (resultSet, rowNum) -> new Customer(resultSet.getLong("id"), resultSet.getString("first_name"), resultSet.getString("last_name"))
        );
        log.info("{}건의 고객정보가 조회되었습니다.", customers.size());

        return customers;
    }
}