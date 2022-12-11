package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CustomerService {

    List<CustomerDto> getCustomers();

    CustomerDto getCustomerById(long id);

    CustomerDto createNewCustomer(CustomerDto newCustomer);

    CustomerDto saveCustomer(long customerId, CustomerDto dto);

    CustomerDto patchCustomer(long id, CustomerDto customerDto);

    void deleteById(Long id);
}
