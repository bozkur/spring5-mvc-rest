package guru.springfamework.spring5mvcrest.bootstrap;

import guru.springfamework.spring5mvcrest.domain.Category;
import guru.springfamework.spring5mvcrest.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        Category fruits = createCategory("Fruits");
        Category dried = createCategory("Dried");
        Category fresh = createCategory("Fresh");
        Category exotic = createCategory("Exotic");
        Category nuts = createCategory("Nuts");

        Stream.of(fruits, fresh, dried, exotic, nuts).forEach(categoryRepository::save);

        log.info("Number of categories in system: {}", categoryRepository.count());
    }

    private Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
