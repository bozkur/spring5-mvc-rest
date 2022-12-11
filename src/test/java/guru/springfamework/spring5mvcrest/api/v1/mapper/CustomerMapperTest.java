package guru.springfamework.spring5mvcrest.api.v1.mapper;

import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import guru.springfamework.spring5mvcrest.domain.Customer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class CustomerMapperTest {

    @Test
    void shouldConvertToDto() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        CustomerDto dto = CustomerMapper.INSTANCE.toDto(customer);

        assertThat(dto.getFirstname(), Matchers.equalTo(customer.getFirstName()));
        assertThat(dto.getLastname(), Matchers.equalTo(customer.getLastName()));
        assertThat(dto.getUrl(), Matchers.equalTo("/shop/customers/" + customer.getId()));
    }

}