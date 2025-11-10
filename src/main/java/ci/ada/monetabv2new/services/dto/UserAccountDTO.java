
package ci.ada.monetabv2new.services.dto;


import ci.ada.monetabv2new.models.enumeration.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {
    private Long id;


    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    @NotNull(message = "Le username est obligatoire")
    private String username;


    private String password;

}
