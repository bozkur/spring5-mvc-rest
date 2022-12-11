package guru.springfamework.spring5mvcrest.api.v1.mapper;

import guru.springfamework.spring5mvcrest.api.v1.model.VendorDto;
import guru.springfamework.spring5mvcrest.controller.v1.VendorController;
import guru.springfamework.spring5mvcrest.domain.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class VendorMapper {

    public static final VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "url", qualifiedByName = "IdToUrl")
    public abstract VendorDto toDto(Vendor vendor);

    @Mapping(source = "name", target = "name")
    public abstract Vendor toDomain(VendorDto dto);

    @Named("IdToUrl")
    String mapUrl(Long id) {
        return  "/shop/vendors/" +id;
    }
}
