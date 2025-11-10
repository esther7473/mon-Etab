package ci.ada.monetabv2new.services.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class EmploiDTO {
    private Long id;

    @NotNull(message = "La classe est obligatoire")
    private ClassDTO classe;

    private List<CoursDTO> cours;

    @NotNull(message = "Le semestre est obligatoire")
    @Min(value = 1, message = "Le semestre doit être positif")
    private Integer semestre;

    @NotBlank(message = "L'année académique est obligatoire")
    private String anneeAcademique;
}