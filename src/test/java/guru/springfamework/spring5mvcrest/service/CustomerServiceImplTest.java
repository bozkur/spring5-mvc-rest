package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import guru.springfamework.spring5mvcrest.domain.Customer;
import guru.springfamework.spring5mvcrest.repositories.CustomerRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;
    @InjectMocks
    private CustomerServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetCustomers() {
        Customer johnB = createCustomer("John", "Brown", 1L);
        Customer aliceW = createCustomer("Alice", "White", 2L);
        when(repository.findAll()).thenReturn(Arrays.asList(johnB, aliceW));

        List<CustomerDto> dtos = service.getCustomers();

        assertThat(dtos, Matchers.hasSize(2));
        compareDtoToCustomer(dtos.get(0), johnB);
        compareDtoToCustomer(dtos.get(1), aliceW);
    }

    private void compareDtoToCustomer(CustomerDto dto, Customer customer) {
        assertThat(dto.getFirstname(), Matchers.equalTo(customer.getFirstName()));
        assertThat(dto.getLastname(), Matchers.equalTo(customer.getLastName()));
        assertThat(dto.getUrl(), Matchers.equalTo("/shop/customers/" + customer.getId()));
    }

    private Customer createCustomer(String name, String lastname, long id) {
        Customer customer = new Customer();
        customer.setFirstName(name);
        customer.setLastName(lastname);
        customer.setId(id);
        return customer;
    }

    @Test
    void shouldGetCustomerWithId() {
        long id = 1L;
        Customer customer = createCustomer("dave", "black", id);
        when(repository.findById(id)).thenReturn(Optional.of(customer));

        CustomerDto obtained = service.getCustomerById(id);

        assertThat(obtained.getFirstname(), Matchers.equalTo(customer.getFirstName()));
        assertThat(obtained.getLastname(), Matchers.equalTo(customer.getLastName()));
    }

    @Test
    void shouldCreateNewCustomer() {
        Customer customer = createCustomer("fred", "çakmaktaş", 1L);
        when(repository.save(any())).thenReturn(customer);
        CustomerDto newCustomer = new CustomerDto();
        newCustomer.setFirstname(customer.getFirstName());
        newCustomer.setLastname(customer.getLastName());

        CustomerDto createdCustomer = service.createNewCustomer(newCustomer);

        assertThat(createdCustomer.getLastname(), Matchers.equalTo(customer.getLastName()));
        assertThat(createdCustomer.getFirstname(), Matchers.equalTo(customer.getFirstName()));
        assertThat(createdCustomer.getUrl(), Matchers.equalTo("/shop/customers/" + customer.getId()));
    }

    @Test
    void shouldSaveCustomer() {
        CustomerDto dto = new CustomerDto();
        dto.setFirstname("Fred");
        dto.setLastname("Moloztaş");
        long customerId = 4L;

        Customer fred = createCustomer(dto.getFirstname(), "Çakmaktaş", customerId);
        when(repository.findById(customerId)).thenReturn(Optional.of(fred));

        Customer updatedCustomer = createCustomer(dto.getFirstname(), dto.getLastname(), customerId);
        when(repository.save(any())).thenReturn(updatedCustomer);

        CustomerDto savedCustomer = service.saveCustomer(customerId, dto);

        assertThat(savedCustomer.getFirstname(), Matchers.equalTo(dto.getFirstname()));
        assertThat(savedCustomer.getLastname(), Matchers.equalTo(dto.getLastname()));
        assertThat(savedCustomer.getUrl(), Matchers.equalTo("/shop/customers/" + fred.getId()));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCustomerWithIdCanNotBeFound() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.getCustomerById(1L));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenResourceToBeUpdatedCanNotBeFound() {
        when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.saveCustomer(1L, new CustomerDto()));
    }

}