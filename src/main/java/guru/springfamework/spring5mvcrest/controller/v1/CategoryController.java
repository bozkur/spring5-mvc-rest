package guru.springfamework.spring5mvcrest.controller.v1;

import guru.springfamework.spring5mvcrest.api.v1.model.CategoryDto;
import guru.springfamework.spring5mvcrest.api.v1.model.CategoryListDto;
import guru.springfamework.spring5mvcrest.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/categories")
@Controller
public class CategoryController {

    public static final String CATEGORY_URL = "/api/v1/categories";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryListDto> getCategories() {
        List<CategoryDto> categories = categoryService.getCategories();
        return new ResponseEntity<>(new CategoryListDto(categories), HttpStatus.OK);
    }

    @GetMapping("{name}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name) {
        CategoryDto category = categoryService.getCategoryByName(name);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
