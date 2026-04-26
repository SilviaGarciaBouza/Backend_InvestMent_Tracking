package com.silviagarcia.investtracking.investment_tracking;

import com.silviagarcia.investtracking.investment_tracking.dto.CategoryDTO;
import com.silviagarcia.investtracking.investment_tracking.model.Category;
import com.silviagarcia.investtracking.investment_tracking.repository.CategoryRepository;
import com.silviagarcia.investtracking.investment_tracking.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CategoryService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CategoryBottomUpIntegrationTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    void repositoryPersists_and_serviceReturnsDto() {
        // given
        Category cat = new Category();
        cat.setName("Acciones");
        em.persistAndFlush(cat);

        // when
        List<CategoryDTO> dtos = categoryService.getAllCategories();

        // then
        assertThat(dtos).isNotEmpty();
        assertThat(dtos).extracting(CategoryDTO::getName).contains("Acciones");
    }
}

