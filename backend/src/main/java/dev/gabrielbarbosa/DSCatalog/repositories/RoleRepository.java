package dev.gabrielbarbosa.DSCatalog.repositories;

import dev.gabrielbarbosa.DSCatalog.entities.Role;
import dev.gabrielbarbosa.DSCatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
