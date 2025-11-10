package ci.ada.monetabv2new.services;


import ci.ada.monetabv2new.services.dto.NoteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NoteService extends CRUDService<NoteDTO> {
    List<NoteDTO> getNotesByStudent(Long studentId);
    List<NoteDTO> getNotesByEvaluation(Long evaluationId);
    void saveAll(List<NoteDTO> notes);
}
