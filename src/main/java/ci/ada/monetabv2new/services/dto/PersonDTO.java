package ci.ada.monetabv2new.services.dto;

import ci.ada.monetabv2new.models.enumeration.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PersonDTO {
    private Long id;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private String tel;
    private String pays;
    private String photo;
    private String slug;
    private LocalDate birthDate;


    @NotBlank(message = "Le login est obligatoire")
    private String login;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

}
