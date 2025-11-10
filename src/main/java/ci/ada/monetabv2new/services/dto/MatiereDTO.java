package ci.ada.monetabv2new.services.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MatiereDTO {
    private Long id;

    @NotBlank(message = "Le nom de la matière est obligatoire")
    private String nom;

    @NotBlank(message = "Le code de la matière est obligatoire")
    private String code;


}