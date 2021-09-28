package kdt.spring.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@SpringBootTest
public class JdbcTemplateTest {

    static final String DROP_TABLE_SQL = "DROP TABLE customers IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";
    static final String INSERT_SQL = "INSERT INTO customers (id, first_name, last_name) VALUES(1, 'honggu', 'kang')";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void setJdbcTemplate_sample() {
        jdbcTemplate.update(DROP_TABLE_SQL);
        jdbcTemplate.update(CREATE_TABLE_SQL);
        log.info("CREATED TABLE USING JDBC TEMPLATE");

        jdbcTemplate.update(INSERT_SQL);
        log.info("INSERTED CUSTOMER INFORMATION USING JDBC TEMPLATE");

        String fullName = jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE id = 1",
                (resultSet, i) -> resultSet.getString("first_name") + " " + resultSet.getString("last_name")
        );
        log.info("Full_NAME : {}", fullName);
    }
}