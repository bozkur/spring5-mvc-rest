package guru.springfamework.spring5mvcrest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VendorListDto {
    private List<VendorDto> vendors;
}