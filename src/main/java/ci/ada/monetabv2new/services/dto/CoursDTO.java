package ci.ada.monetabv2new.services.dto;


import ci.ada.monetabv2new.models.enumeration.TypeCours;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CoursDTO {

    private Long id;

    @NotNull(message = "L'enseignant est obligatoire")
    private TeacherDTO enseignant;

    @NotNull(message = "La classe est obligatoire")
    private ClassDTO classe;

    private String salle;
    private LocalDate dateCours;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private TypeCours typeCours;
}
