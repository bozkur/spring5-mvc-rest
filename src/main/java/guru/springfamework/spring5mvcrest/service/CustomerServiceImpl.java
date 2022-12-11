package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.mapper.CustomerMapper;
import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import guru.springfamework.spring5mvcrest.domain.Customer;
import guru.springfamework.spring5mvcrest.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;


    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
        this.mapper = CustomerMapper.INSTANCE;
    }

    @Override
    public List<CustomerDto> getCustomers() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomerById(long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public CustomerDto createNewCustomer(CustomerDto newCustomer) {
        Customer customer = mapper.toDomain(newCustomer);
        return mapper.toDto(repository.save(customer));
    }

    @Override
    public CustomerDto saveCustomer(long customerId, CustomerDto dto) {
        Optional<Customer> customerOptional = repository.findById(customerId);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException("Customer with id " + customerId + " can not be found.");
        }
        Customer target = mapper.toDomain(dto);
        target.setId(customerId);
        return mapper.toDto(repository.save(target));
    }

    @Override
    public CustomerDto patchCustomer(long id, CustomerDto customerDto) {
        Optional<Customer> customerOptional = repository.findById(id);

        Customer targetCustomer = customerOptional.orElseThrow(ResourceNotFoundException::new);
        updateForPatch(targetCustomer, customerDto);
        return mapper.toDto(repository.save(targetCustomer));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void updateForPatch(Customer targetCustomer, CustomerDto customerDto) {
        if (customerDto.getFirstname() != null) {
            targetCustomer.setFirstName(customerDto.getFirstname());
        }
        if (customerDto.getLastname() != null) {
            targetCustomer.setLastName(customerDto.getLastname());
        }
    }
}
