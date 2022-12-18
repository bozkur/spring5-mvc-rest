package guru.springfamework.spring5mvcrest.controller.v1;

import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import guru.springfamework.spring5mvcrest.api.v1.model.CustomerListDto;
import guru.springfamework.spring5mvcrest.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("Customer Controller APIsi")
@RequestMapping(CustomerController.CUSTOMER_API_URL)
@RestController
public class CustomerController {

    public static final String CUSTOMER_API_URL = "/api/v1/customers";
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @ApiOperation(value = "Müşteri listesini elde eder.", notes = "Müşteri listesi elde etme operasyonu notları.")
    @GetMapping
    public CustomerListDto getCustomers() {
        List<CustomerDto> customerList = service.getCustomers();
        return new CustomerListDto(customerList);
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerWithId(@PathVariable long id) {
        return service.getCustomerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody CustomerDto customer) {
        return service.createNewCustomer(customer);
    }

    @PutMapping("/{id}")
    public CustomerDto saveCustomer(@PathVariable Long id, @RequestBody CustomerDto newCustomer) {
        return service.saveCustomer(id, newCustomer);
    }

    @PatchMapping("/{id}")
    public CustomerDto patchCustomer(@PathVariable Long id, @RequestBody CustomerDto patchCustomer) {
        return service.patchCustomer(id, patchCustomer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        service.deleteById(id);
    }
}
