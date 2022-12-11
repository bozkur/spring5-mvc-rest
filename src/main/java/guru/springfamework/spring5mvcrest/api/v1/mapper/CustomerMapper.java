package guru.springfamework.spring5mvcrest.api.v1.mapper;

import guru.springfamework.spring5mvcrest.api.v1.model.CustomerDto;
import guru.springfamework.spring5mvcrest.domain.Customer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class CustomerMapper {

    public static final CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "lastName", target = "lastname")
    @Mapping(source = "firstName", target = "firstname")
    @Mapping(source = "id", target="url", qualifiedByName = "IdToString")
    public abstract CustomerDto toDto(Customer customer);

    @Mapping(source = "lastname", target = "lastName")
    @Mapping(source = "firstname", target = "firstName")
    public abstract Customer toDomain(CustomerDto dto);

    @Named("IdToString")
    String mapUrl(Long id) {
        return "/shop/customers/" + id;
    }
}
