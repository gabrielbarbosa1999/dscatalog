package dev.gabrielbarbosa.DSCatalog.services;

import dev.gabrielbarbosa.DSCatalog.dto.ProductDTO;
import dev.gabrielbarbosa.DSCatalog.entities.Category;
import dev.gabrielbarbosa.DSCatalog.entities.Product;
import dev.gabrielbarbosa.DSCatalog.repositories.CategoryRepository;
import dev.gabrielbarbosa.DSCatalog.repositories.ProductRepository;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.DatabaseException;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(product -> new ProductDTO(product, product.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PRODUTO NÃO ENCONTRADA."));
        return new ProductDTO(product, product.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        productDTO.getCategories().forEach(category -> {
            Category referenceById = categoryRepository.getReferenceById(category.getId());
            product.getCategories().add(referenceById);
        });
        return new ProductDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product = productRepository.getReferenceById(id);
            product.update(productDTO);
            productDTO.getCategories().forEach(category -> {
                Category referenceById = categoryRepository.findById(category.getId()).get();
                product.getCategories().add(referenceById);
            });
            return new ProductDTO(productRepository.save(product));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("PRODUCT ID " + id + " NAO ENCONTRADA.");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("PRODUCT ID " + id + " NAO ENCONTRADA.");
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("ESTE PRODUCT ESTÁ SENDO USADA.");
        }
    }

}
