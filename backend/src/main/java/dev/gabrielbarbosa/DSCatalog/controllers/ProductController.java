package dev.gabrielbarbosa.DSCatalog.controllers;

import dev.gabrielbarbosa.DSCatalog.dto.ProductDTO;
import dev.gabrielbarbosa.DSCatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        Page<ProductDTO> products = productService.findAll(pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.insert(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.update(id, productDTO);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
