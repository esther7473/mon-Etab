package ci.ada.monetabv2new.services.dto;

import ci.ada.monetabv2new.models.enumeration.TypeEvaluation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTO {
    private Long id;

    @NotNull(message = "L'enseignant est obligatoire")
    private TeacherDTO enseignant;

    @NotNull(message = "La classe est obligatoire")
    private ClassDTO classe;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateRemise;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreation;

    @NotNull(message = "Le type d'évaluation est obligatoire")
    private TypeEvaluation typeEvaluation;
}
