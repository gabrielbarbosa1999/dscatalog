package dev.gabrielbarbosa.DSCatalog.services;

import dev.gabrielbarbosa.DSCatalog.dto.CategoryDTO;
import dev.gabrielbarbosa.DSCatalog.entities.Category;
import dev.gabrielbarbosa.DSCatalog.repositories.CategoryRepository;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CATEGORIA NÃO ENCONTRADA."));
        return new CategoryDTO(category);
    }

    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category category = categoryRepository.save(new Category(categoryDTO));
        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO updated(Long id, CategoryDTO categoryDTO) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(categoryDTO.getName());
            return new CategoryDTO(categoryRepository.save(category));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("CATEGORY ID " + id + " NAO ENCONTRADA.");
        }
    }
}
