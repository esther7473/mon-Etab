package ci.ada.monetabv2new.services.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDTO {


    private Long id;
    private Double valeur;
    private Double coefficient;


    private EvaluationDTO evaluation;
    private String appreciation;

    private StudentDTO etudiant;


    private TeacherDTO enseignant;
}
