package dev.gabrielbarbosa.DSCatalog.services;

import dev.gabrielbarbosa.DSCatalog.dto.ProductDTO;
import dev.gabrielbarbosa.DSCatalog.entities.Category;
import dev.gabrielbarbosa.DSCatalog.entities.Product;
import dev.gabrielbarbosa.DSCatalog.repositories.CategoryRepository;
import dev.gabrielbarbosa.DSCatalog.repositories.ProductRepository;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.DatabaseException;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId, nonExistingId, dependentId, categoryId, nonExistingCategoryId;

    private Product product;

    private Category category, categoryNotExistinng;

    private PageImpl<Product> page;


    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        categoryId = 1L;
        nonExistingCategoryId = 2L;
        product = new Product(4L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        category = new Category(categoryId, "Phones");
        categoryNotExistinng = new Category(nonExistingCategoryId, "Phones");
        page = new PageImpl<>(List.of(product));

        when(productRepository.existsById(existingId)).thenReturn(true);
        when(productRepository.existsById(nonExistingId)).thenReturn(false);
        when(productRepository.existsById(dependentId)).thenReturn(true);

        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(productRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(categoryRepository.getReferenceById(categoryId)).thenReturn(category);
        when(categoryRepository.getReferenceById(nonExistingCategoryId)).thenThrow(ResourceNotFoundException.class);


        when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }

    @Test
    public void findAllPagedShoutReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> resulta = productService.findAll(pageable);

        Assertions.assertNotNull(resulta);
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTO() {
        ProductDTO result = productService.findById(existingId);

        Assertions.assertNotNull(result);
        verify(productRepository, times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(nonExistingId);
        });
    }

    @Test
    public void updateShouldReturnProductDTO() {
        Product productUpdated = new Product(existingId, "Phone Update", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        ProductDTO productDTO = new ProductDTO(productUpdated, Set.of(category));
        ProductDTO result = productService.update(existingId, productDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Product productUpdated = new Product(nonExistingId, "Phone Update", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
            ProductDTO productDTO = new ProductDTO(productUpdated, Set.of(category));
            productService.update(nonExistingId, productDTO);
        });
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenCategoryIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Product productUpdated = new Product(existingId, "Phone Update", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
            ProductDTO productDTO = new ProductDTO(productUpdated, Set.of(categoryNotExistinng));
            productService.update(existingId, productDTO);
        });
    }

}
