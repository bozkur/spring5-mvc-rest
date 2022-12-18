package guru.springfamework.spring5mvcrest.api.v1.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class VendorDto {
    @ApiModelProperty(value = "Name of the vendor", required = true)
    private String name;
    @ApiModelProperty(value = "Url of the vendor", notes = "Created by using id of the vendor.")
    private String url;
}
