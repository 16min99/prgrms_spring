package org.prgrms.kdt.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//spring 을 이용한 jdbc
@Repository
public class CustomerJdbcRepositoryNoJDBCTemplate implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerJdbcRepositoryNoJDBCTemplate.class);
    private final DataSource dataSource;

    public CustomerJdbcRepositoryNoJDBCTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Customer insert(Customer customer) {
        try (
                Connection connection = dataSource.getConnection();
                var statement = connection.prepareStatement("insert into customers(customer_id, name, email, created_at) VALUES (UUID_TO_BIN(?), ?, ?, ?) ");
        ) {
            statement.setBytes(1, customer.getCustomerId().toString().getBytes());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setTimestamp(4, Timestamp.valueOf(customer.getCreatedAt()));
            int executeUpdate = statement.executeUpdate();
            if (executeUpdate != 1) {
                throw new RuntimeException("Noting was inserted");
            }
            return customer;
        } catch (SQLException e) {
            logger.error("Got error while closing connection", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer update(Customer customer) {
        try (
                Connection connection = dataSource.getConnection();
                var statement = connection.prepareStatement("UPDATE customers SET name = ?, email = ?, last_login_at = ? WHERE customer_id = UUID_TO_BIN(?)");
        ) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setTimestamp(3, customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null);
            statement.setBytes(4, customer.getCustomerId().toString().getBytes());
            var executeUpdate = statement.executeUpdate();
            if (executeUpdate != 1) {
                throw new RuntimeException("Noting was updated");
            }
            return customer;
        } catch (SQLException e) {
            logger.error("Got error while closing connection", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers");
                var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                mapToCustomer(allCustomers, resultSet);
            }
        } catch (SQLException e) {
            logger.error("Got error while closing connection", e);
            throw new RuntimeException(e);
        }
        return allCustomers;
    }

    @Override
    public Optional<Customer> findById(UUID cutomerId) {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE customer_id = UUID_TO_BIN(?)");//SELECT_BY_NAME_SQL);
        ) {
            statement.setBytes(1, cutomerId.toString().getBytes());// 1-> ?의 순서
            logger.info("statement -> {}", statement);// ''안으로 몽땅 들어가게 된다 즉, or 절이 발동 하지 않는다.
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Got error while closing connection", e);
            throw new RuntimeException(e);
        }
        return allCustomers.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByName(String name) {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE name = ?");//SELECT_BY_NAME_SQL);
        ) {
            statement.setString(1, name);// 1-> ?의 순서
            logger.info("statement -> {}", statement);// ''안으로 몽땅 들어가게 된다 즉, or 절이 발동 하지 않는다.
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Got error while closing connection", e);
        }
        return allCustomers.stream().findFirst();
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        List<Customer> allCustomers = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                var statement = connection.prepareStatement("select * from customers WHERE email = ?");//SELECT_BY_NAME_SQL);
        ) {
            statement.setString(1, email);// 1-> ?의 순서
            logger.info("statement -> {}", statement);// ''안으로 몽땅 들어가게 된다 즉, or 절이 발동 하지 않는다.
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    mapToCustomer(allCustomers, resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Got error while closing connection", e);
        }
        return allCustomers.stream().findFirst();
    }

    @Override
    public void deleteAll() {
        try (
                Connection connection = dataSource.getConnection();
                var statement = connection.prepareStatement("DELETE FROM customers");
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Got error while closing connection", e);
            throw new RuntimeException(e);
        }
    }

    private void mapToCustomer(List<Customer> allCustomers, java.sql.ResultSet resultSet) throws SQLException {
        String customerName = resultSet.getString("name");
        String email = resultSet.getString("email");
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        //logger.info("customer id-> {}, customer name-> {}, createdAt -> {}", customerId, customerName, createdAt);
        allCustomers.add(new Customer(customerId, customerName, email, lastLoginAt, createdAt));
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    @Override
    public int count(){
        return 1;//구현하기
    }

//    @Override
//    public List<Customer> findByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
//        return null;//구현하기
//    }

}
