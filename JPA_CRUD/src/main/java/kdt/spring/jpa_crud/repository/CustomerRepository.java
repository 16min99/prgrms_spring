package kdt.spring.jpa_crud.repository;

import kdt.spring.jpa_crud.repository.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> { //엔티티, Id
}
