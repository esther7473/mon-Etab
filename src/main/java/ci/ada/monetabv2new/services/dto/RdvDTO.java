package ci.ada.monetabv2new.services.dto;


import ci.ada.monetabv2new.models.enumeration.Statut;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class RdvDTO {
    private Long id;

    private StudentDTO student;

    private TeacherDTO teacher;

    private LocalDate date;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private Statut statut;

    private String motif;
}
