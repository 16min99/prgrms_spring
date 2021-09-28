package kdtspring.mybatis.repository;

import kdtspring.mybatis.repository.domain.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomerAnnotationMapper {
    @Insert("INSERT INTO customers (id, first_name, last_name) VALUES(#{id}, #{firstName}, #{lastName})")
    void save(Customer customer);

    @Update("UPDATE customers SET first_name=#{firstName}, last_name=#{lastName} WHERE id=#{id}")
    void update(Customer customer);

    @Select("SELECT * FROM customers")
    List<Customer> findAll();

    @Select("SELECT * FROM customers WHERE id = #{id}")
    Customer findById(@Param("id") long id);
}