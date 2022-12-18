package guru.springfamework.spring5mvcrest.api.v1.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
public class VendorListDto {
    @ApiModelProperty(value = "List of vendors")
    private List<VendorDto> vendors;
}
