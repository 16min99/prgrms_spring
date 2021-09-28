package kdtspring.mybatis;

import kdtspring.mybatis.repository.CustomerXmlMapper;
import kdtspring.mybatis.repository.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
class CustomerXmlMapperTest {
    static final String DROP_TABLE_SQL = "DROP TABLE customers IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";

    //TABLE 생성의 경우 Query명시를 하지 않았기 때문에 jdbc를 이용
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CustomerXmlMapper customerXmlMapper;

    @BeforeEach
    void setUp(){
        //jdbc
        jdbcTemplate.update(DROP_TABLE_SQL);
        jdbcTemplate.update(CREATE_TABLE_SQL);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("TRUNCATE TABLE customers");
    }

    @Test
    @DisplayName("고객정보가 저장되는지 확인한다")
    void testSave() {
        // Given
        Customer customer = new Customer(1L,"minkyu","jeon");

        // When
        customerXmlMapper.save(customer);

        // Then
        Customer guppy = customerXmlMapper.findById(1);
        assertThat(guppy.getFirstName()).isEqualTo("minkyu");
    }

    @Test
    void 고객정보가_수정되는지_확인한다() { //DisplayName을 쓰지않고 이렇게 해도 괜찮넹
        // Given
        Customer customer = new Customer(1L, "honggu", "kang");
        customerXmlMapper.save(customer);
        customer.setFirstName("guppy");
        customer.setLastName("hong");

        // When
        customerXmlMapper.update(customer);

        // Then
        Customer updated = customerXmlMapper.findById(1);
        assertThat(updated.getLastName()).isEqualTo(customer.getLastName());
        assertThat(updated.getFirstName()).isEqualTo(customer.getFirstName());
    }

    @Test
    void 단건조회를_확인한다() {
        // Given
        Customer customer = new Customer(1L, "honggu", "kang");
        customerXmlMapper.save(customer);

        // When
        Customer selected = customerXmlMapper.findById(customer.getId());

        // Then
        assertThat(customer.getId()).isEqualTo(selected.getId());
    }

    @Test
    void 리스트조회를_확인한다() {
        // Given
        Customer customer1 = new Customer(1L, "honggu", "kang");
        Customer customer2 = new Customer(2L, "guppy", "hong");

        customerXmlMapper.save(customer1);
        customerXmlMapper.save(customer2);

        // When
        List<Customer> selectedCustomers = customerXmlMapper.findAll();

        // Then
        assertThat(selectedCustomers.size()).isEqualTo(2);
    }
}