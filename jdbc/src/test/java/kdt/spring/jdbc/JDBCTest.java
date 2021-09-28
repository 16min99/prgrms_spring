package kdt.spring.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
public class JDBCTest {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "sa";
    static final String PASS = "";

    static final String DROP_TABLE_SQL = "DROP TABLE customers IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";
    static final String INSERT_SQL = "INSERT INTO customers (id, first_name, last_name) VALUES(1, 'honggu', 'kang')";

    @Test
    void jdbc_sample() {
        try {
            // 어떤 드라이브를 사용할지 명시
            Class.forName(JDBC_DRIVER);

            // Connection 획득
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            log.info("Connection 획득");

            // 획득한 Connection을 통해 Statement 객체 생성
            Statement statement = connection.createStatement();
            log.info("Statement 획득");

            // Statement 객체를 통해 통신
            log.info("쿼리 실행");
            statement.executeUpdate(DROP_TABLE_SQL);
            statement.executeUpdate(CREATE_TABLE_SQL);
            log.info("CREATED TABLE");

            statement.executeUpdate(INSERT_SQL);

            // ResultSet 객체를 통해 결과값을 조회 가능 (조회된 low 정보를 저장)
            ResultSet resultSet = statement.executeQuery("SELECT id, first_name, last_name FROM customers WHERE id = 1");

            while (resultSet.next()) {
                String fullName = resultSet.getString("first_name")+ " " + resultSet.getString("last_name");
                log.info("CUSTOMER FULL_NAME : {}", fullName);
            }

            // 사용한 객체 반납
            log.info("반납, 반납");
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}