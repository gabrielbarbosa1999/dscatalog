package dev.gabrielbarbosa.DSCatalog.services;

import dev.gabrielbarbosa.DSCatalog.dto.UserDTO;
import dev.gabrielbarbosa.DSCatalog.dto.UserInsertDTO;
import dev.gabrielbarbosa.DSCatalog.entities.Category;
import dev.gabrielbarbosa.DSCatalog.entities.Role;
import dev.gabrielbarbosa.DSCatalog.entities.User;
import dev.gabrielbarbosa.DSCatalog.repositories.CategoryRepository;
import dev.gabrielbarbosa.DSCatalog.repositories.RoleRepository;
import dev.gabrielbarbosa.DSCatalog.repositories.UserRepository;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.DatabaseException;
import dev.gabrielbarbosa.DSCatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("USER NÃO ENCONTRADA."));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO userDTO) {
        String password = encoder.encode(userDTO.getPassword());
        User user = new User(userDTO, password);
        userDTO.getRoles().forEach(roleEnviada -> {
            Role role = roleRepository.findById(roleEnviada.getId()).orElseThrow(() -> new ResourceNotFoundException("ROLE ID " + roleEnviada.getId() + " NÃO ENCONTRADA."));
            user.adicionarRole(role);
        });
        return new UserDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO update(Long id, UserInsertDTO userDTO) {
        User user = userRepository.getReferenceById(id);
        user.limparRoles();
        user.update(userDTO);
        userDTO.getRoles().forEach(roleEnviada -> {
            Role role = roleRepository.findById(roleEnviada.getId()).orElseThrow(() -> new ResourceNotFoundException("ROLE ID " + roleEnviada.getId() + " NÃO ENCONTRADA."));
            user.adicionarRole(role);
        });
        return new UserDTO(userRepository.save(user));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("USER ID " + id + " NAO ENCONTRADA.");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("ESTE USER ESTÁ SENDO USADA.");
        }
    }

}
