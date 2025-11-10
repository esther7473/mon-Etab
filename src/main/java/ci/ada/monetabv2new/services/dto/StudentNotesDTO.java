package ci.ada.monetabv2new.services.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// DTO pour structurer les données
@Getter
@Setter
public class StudentNotesDTO {
    private StudentDTO student;
    private List<NoteDTO> notes;
    private Double moyenne;
}