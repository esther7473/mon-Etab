package ci.ada.monetabv2new.services.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StudentDTO extends PersonDTO {
    private Long id;

    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    private ClassDTO classDTO;
    private UserAccountDTO userAccount;


    private ParentDTO parent;
}