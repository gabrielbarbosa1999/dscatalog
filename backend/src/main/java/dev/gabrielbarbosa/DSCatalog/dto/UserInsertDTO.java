package dev.gabrielbarbosa.DSCatalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

public class UserInsertDTO {

    @NotBlank(message = "Campo obrigatório")
    private String firstName;

    private String lastName;

    @Email(message = "Favor digitar email válido")
    private String email;

    private String password;

    private Set<RoleDTO> roles = new HashSet<>();

    public UserInsertDTO() {}

    public UserInsertDTO(String firstName, String lastName, String email, String password, Set<RoleDTO> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

}
