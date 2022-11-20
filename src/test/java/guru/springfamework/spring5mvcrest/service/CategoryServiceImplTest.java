package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.model.CategoryDto;
import guru.springfamework.spring5mvcrest.domain.Category;
import guru.springfamework.spring5mvcrest.repositories.CategoryRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(repository);
    }

    @Test
    void shouldGetCategories() {

        Category fruits = createCategory("Fruits", 1L);
        Category dried = createCategory("Dried", 2L);

        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(fruits, dried));

        List<CategoryDto> categories = categoryService.getCategories();

        MatcherAssert.assertThat(categories, Matchers.hasSize(2));
        MatcherAssert.assertThat(categories.get(0).getName(), Matchers.equalTo(fruits.getName()));
        MatcherAssert.assertThat(categories.get(0).getId(), Matchers.equalTo(fruits.getId()));
        MatcherAssert.assertThat(categories.get(1).getName(), Matchers.equalTo(dried.getName()));
        MatcherAssert.assertThat(categories.get(1).getId(), Matchers.equalTo(dried.getId()));
    }

    @Test
    void shouldGetCategoryByName() {
        Category fruits = createCategory("Fruits", 1L);
        Mockito.when(repository.findByName(fruits.getName())).thenReturn(fruits);

        CategoryDto dto = categoryService.getCategoryByName(fruits.getName());

        MatcherAssert.assertThat(dto.getName(), Matchers.equalTo(fruits.getName()));
        MatcherAssert.assertThat(dto.getId(), Matchers.equalTo(fruits.getId()));
    }

    @Test
    void shouldGetCategoryByNameReturnNullWhenCategoryCannotBeFound() {
        String catName = "fruits";

        CategoryDto dto = categoryService.getCategoryByName(catName);

        Assertions.assertNull(dto);
    }

    private Category createCategory(String name, long id) {
        Category category = new Category();
        category.setName(name);
        category.setId(id);
        return category;
    }
}