package guru.springfamework.spring5mvcrest.api.v1.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Customer Dto", description = "Represents a customer")
@Data
public class CustomerDto {

    @ApiModelProperty(value = "First name", required = true)
    private String firstname;
    private String lastname;
    private String url;
}
