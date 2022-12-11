package guru.springfamework.spring5mvcrest.controller.v1;

import guru.springfamework.spring5mvcrest.api.v1.model.CategoryDto;
import guru.springfamework.spring5mvcrest.service.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController controller;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldGetCategories() throws Exception {
        CategoryDto cat1 = createCategory("Fruits", 1L);
        CategoryDto cat2 = createCategory("Dried", 2L);
        when(categoryService.getCategories()).thenReturn(Arrays.asList(cat1, cat2));

        mockMvc.perform(MockMvcRequestBuilders.get(CategoryController.CATEGORY_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories", Matchers.hasSize(2)));
    }

    @Test
    void shouldGetCategoryByName() throws Exception {
        String name = "Fruits";
        CategoryDto cat1 = createCategory(name, 1L);
        when(categoryService.getCategoryByName(name)).thenReturn(cat1);

        mockMvc.perform(MockMvcRequestBuilders.get(CategoryController.CATEGORY_URL + "/" + name))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(name)));
    }

    private CategoryDto createCategory(String name, long id) {
        CategoryDto dto = new CategoryDto();
        dto.setName(name);
        dto.setId(id);
        return dto;
    }
}