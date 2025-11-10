package ci.ada.monetabv2new.services.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDataDTO {

    private StudentDTO student;

    private List<NoteDTO> notes;
    private Double moyenne;

    private List<CoursDTO> cours;
    private List<EvaluationDTO> evaluations;
    private List<AbsenceDTO> absences;

    private Double assiduite;



}
