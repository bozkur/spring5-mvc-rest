package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import guru.springfamework.spring5mvcrest.bootstrap.Bootstrap;
import guru.springfamework.spring5mvcrest.domain.Customer;
import guru.springfamework.spring5mvcrest.repositories.CategoryRepository;
import guru.springfamework.spring5mvcrest.repositories.CustomerRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CustomerServiceImplIT {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        ReflectionTestUtils.setField(bootstrap, "categoryRepository", categoryRepository);
        ReflectionTestUtils.setField(bootstrap, "customerRepository", customerRepository);
        bootstrap.run();
        customerService = new CustomerServiceImpl(customerRepository);
    }

    @Test
    void shouldPatchCustomerFirstName() {
        List<Customer> customers = customerRepository.findAll();
        Customer targetCustomer = customers.get(0);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstname("Fredrick");

        CustomerDto patchedCustomer = customerService.patchCustomer(targetCustomer.getId(), customerDto);

        assertThat(patchedCustomer.getFirstname(), Matchers.equalTo(customerDto.getFirstname()));
        assertThat(patchedCustomer.getLastname(), Matchers.equalTo(targetCustomer.getLastName()));
        assertThat(patchedCustomer.getUrl(), Matchers.equalTo("/shop/customers/" + targetCustomer.getId()));

    }

    @Test
    void shouldPatchCustomerLastName() {
        List<Customer> customers = customerRepository.findAll();
        Customer targetCustomer = customers.get(0);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setLastname("Barbarossa");

        CustomerDto patchedCustomer = customerService.patchCustomer(targetCustomer.getId(), customerDto);

        assertThat(patchedCustomer.getFirstname(), Matchers.equalTo(targetCustomer.getFirstName()));
        assertThat(patchedCustomer.getLastname(), Matchers.equalTo(customerDto.getLastname()));
        assertThat(patchedCustomer.getUrl(), Matchers.equalTo("/shop/customers/" + targetCustomer.getId()));
    }

    @Test
    void shouldDeleteCustomer() {
        List<Customer> customers = customerRepository.findAll();
        Customer targetCustomer = customers.get(0);

        customerService.deleteById(targetCustomer.getId());

        Optional<Customer> found = customerRepository.findById(targetCustomer.getId());
        Assert.assertTrue(found.isEmpty());
    }
}
