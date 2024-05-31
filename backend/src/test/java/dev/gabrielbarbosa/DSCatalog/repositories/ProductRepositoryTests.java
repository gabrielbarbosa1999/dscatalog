package dev.gabrielbarbosa.DSCatalog.repositories;

import dev.gabrielbarbosa.DSCatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        Long id = 1L;

        productRepository.deleteById(id);
        Optional<Product> result = productRepository.findById(id);

        Assertions.assertFalse(result.isPresent());
    }

}
