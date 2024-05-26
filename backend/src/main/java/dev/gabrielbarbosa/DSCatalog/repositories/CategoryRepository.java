package dev.gabrielbarbosa.DSCatalog.repositories;

import dev.gabrielbarbosa.DSCatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
