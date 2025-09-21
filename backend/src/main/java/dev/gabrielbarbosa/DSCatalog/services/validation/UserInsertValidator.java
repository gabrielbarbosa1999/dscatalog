package dev.gabrielbarbosa.DSCatalog.services.validation;

import dev.gabrielbarbosa.DSCatalog.controllers.exceptions.FieldMessage;
import dev.gabrielbarbosa.DSCatalog.dto.UserInsertDTO;
import dev.gabrielbarbosa.DSCatalog.entities.User;
import dev.gabrielbarbosa.DSCatalog.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        User user = userRepository.findByEmail(dto.getEmail());
        if (nonNull(user)) {
            FieldMessage fieldMessage = new FieldMessage("email", "Este email já esta cadastrado");
            list.add(fieldMessage);
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}