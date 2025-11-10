package ci.ada.monetabv2new.services.dto;

import ci.ada.monetabv2new.models.enumeration.TypeAbsence;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbsenceDTO {
    private Long id;

    private StudentDTO etudiant;
    private CoursDTO cours;

    @NotNull(message = "Le type d'absence est obligatoire")
    private TypeAbsence typeAbsence;

    private Boolean justifiee = false;
    private String motif;




}