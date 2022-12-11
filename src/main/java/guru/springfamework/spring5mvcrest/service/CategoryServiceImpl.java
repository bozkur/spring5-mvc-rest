package guru.springfamework.spring5mvcrest.service;

import guru.springfamework.spring5mvcrest.api.v1.mapper.CategoryMapper;
import guru.springfamework.spring5mvcrest.api.v1.model.CategoryDto;
import guru.springfamework.spring5mvcrest.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
        this.mapper = CategoryMapper.INSTANCE;
    }

    @Override
    public List<CategoryDto> getCategories() {
        return repository.findAll().stream().map(mapper::categoryToDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        return mapper.categoryToDto(repository.findByName(name));
    }
}
