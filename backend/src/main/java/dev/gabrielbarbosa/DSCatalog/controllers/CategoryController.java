package dev.gabrielbarbosa.DSCatalog.controllers;

import dev.gabrielbarbosa.DSCatalog.dto.CategoryDTO;
import dev.gabrielbarbosa.DSCatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.insert(categoryDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(category);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> updated(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.updated(id, categoryDTO);
        return ResponseEntity.ok().body(category);
    }

}
