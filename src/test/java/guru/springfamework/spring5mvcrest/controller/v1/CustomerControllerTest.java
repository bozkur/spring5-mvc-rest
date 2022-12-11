package guru.springfamework.spring5mvcrest.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import guru.springfamework.spring5mvcrest.service.CustomerService;
import guru.springfamework.spring5mvcrest.service.ResourceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    @Mock
    private CustomerService service;
    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new CustomerControllerAdvice())
                .build();
    }

    @Test
    void shouldGetCustomers() throws Exception {
        CustomerDto dto1 = createDto("John", "Brown");
        CustomerDto dto2 = createDto("Alice", "White");
        when(service.getCustomers()).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.CUSTOMER_API_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customers", Matchers.hasSize(2)));
    }

    private CustomerDto createDto(String firstname, String lastname) {
        CustomerDto dto = new CustomerDto();
        dto.setFirstname(firstname);
        dto.setLastname(lastname);
        return dto;
    }

    @Test
    void shouldGetCustomerWithId() throws Exception {
        long id = 1L;
        CustomerDto customer = createDto("John", "Brown");
        when(service.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.get(createUrlWithId(id)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.equalTo(customer.getFirstname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.equalTo(customer.getLastname())));
    }

    private String createUrlWithId(long id) {
        return CustomerController.CUSTOMER_API_URL + "/" + id;
    }

    @Disabled
    @Test
    void shouldGetCustomerWithIdReturns505WhenACustomerWithTheSuppliedIdCanNotBeFound() throws Exception {
        when(service.getCustomerById(ArgumentMatchers.anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get(createUrlWithId(1L)))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    void shouldCreateACustomer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstname("Fred");
        customerDto.setLastname("Çakmaktaş");
        when(service.createNewCustomer(any())).thenReturn(customerDto);

        String customerAsString = getCustomerAsString(customerDto);
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.CUSTOMER_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private String getCustomerAsString(CustomerDto customerDto) throws JsonProcessingException {
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(customerDto);
    }

    @Test
    void shouldUpdateACustomer() throws Exception {
        CustomerDto savedCustomer = new CustomerDto();
        savedCustomer.setFirstname("Fred");
        savedCustomer.setLastname("Çakmaktaş");
        savedCustomer.setUrl("deneme");
        long customerId = 1L;
        when(service.saveCustomer(eq(customerId), any())).thenReturn(savedCustomer);

        mockMvc.perform(MockMvcRequestBuilders.put(createUrlWithId(customerId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getCustomerAsString(savedCustomer)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.equalTo(savedCustomer.getFirstname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.equalTo(savedCustomer.getLastname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Matchers.equalTo(savedCustomer.getUrl())));


    }

    @Test
    void shouldPatchCustomer() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setFirstname("Fred");
        dto.setLastname("Çakmaktaş");
        long customerId = 1L;

        CustomerDto patch = new CustomerDto();
        patch.setFirstname("Barney");

        CustomerDto patchedDto = new CustomerDto();
        patchedDto.setFirstname(patch.getFirstname());
        patchedDto.setLastname(dto.getLastname());

        when(service.patchCustomer(customerId, patch)).thenReturn(patchedDto);

        mockMvc.perform(MockMvcRequestBuilders.patch(createUrlWithId(customerId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getCustomerAsString(patch)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.equalTo(patch.getFirstname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.equalTo(dto.getLastname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url", Matchers.equalTo(dto.getUrl())));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        long customerId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete(createUrlWithId(customerId)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldRetur404WhenCustomerCanNotBeFound() throws Exception {
        when(service.getCustomerById(1L)).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get(createUrlWithId(1L)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturn404WhenCustomerCanNotBeFoundWhenUpdatingCustomer() throws Exception {
        long customerId = 1L;
        when(service.saveCustomer(ArgumentMatchers.eq(customerId), ArgumentMatchers.any(CustomerDto.class))).thenThrow(new ResourceNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.put(createUrlWithId(customerId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getCustomerAsString(new CustomerDto())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}