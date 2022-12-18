package guru.springfamework.spring5mvcrest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(createMetaData())
                .pathMapping("/");
    }

    private ApiInfo createMetaData() {
        return new ApiInfoBuilder()
                .contact(new Contact("Cevher Bozkur", "https://ccb.org/swagger",
                 "bozkur@gmail.com"))
                .description("Spring 5 framework Swagger example")
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}
