package guru.springfamework.spring5mvcrest.repositories;

import guru.springfamework.spring5mvcrest.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
