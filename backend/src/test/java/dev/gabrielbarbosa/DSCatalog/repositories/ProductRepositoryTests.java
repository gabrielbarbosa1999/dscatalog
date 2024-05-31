package dev.gabrielbarbosa.DSCatalog.repositories;

import dev.gabrielbarbosa.DSCatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Long exintingId;

    @BeforeEach
    void setUp() {
        exintingId = 1L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(exintingId);
        Optional<Product> result = productRepository.findById(exintingId);

        Assertions.assertFalse(result.isPresent());
    }

}
