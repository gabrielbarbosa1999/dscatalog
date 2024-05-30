package dev.gabrielbarbosa.DSCatalog.repositories;

import dev.gabrielbarbosa.DSCatalog.entities.Category;
import dev.gabrielbarbosa.DSCatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
