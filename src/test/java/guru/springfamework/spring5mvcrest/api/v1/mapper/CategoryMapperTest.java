package guru.springfamework.spring5mvcrest.api.v1.mapper;

import guru.springfamework.spring5mvcrest.api.v1.model.CategoryDto;
import guru.springfamework.spring5mvcrest.domain.Category;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {


    @Test
    void shouldMapCategory() {
        Category category = new Category();
        category.setName("Name");
        category.setId(12L);

        CategoryDto dto = CategoryMapper.INSTANCE.categoryToDto(category);

        MatcherAssert.assertThat(dto.getName(), Matchers.equalTo(category.getName()));
        MatcherAssert.assertThat(dto.getId(), Matchers.equalTo(category.getId()));

    }
}