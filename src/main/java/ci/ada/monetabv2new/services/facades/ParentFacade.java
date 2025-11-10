package ci.ada.monetabv2new.services.facades;

import ci.ada.monetabv2new.services.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface ParentFacade {


    // Gestion des parents
    ParentDTO getParent(String login);
    ParentDTO partialUpdate(ParentDTO parentDTO);

    // Gestion des enfants
    List<StudentDTO> getChildrenByParent(Long parentId);
    StudentDTO getStudentById(Long studentId, Long parentId);

    // Gestion des notes et moyennes
    List<NoteDTO> getNotesByStudent(Long studentId);
    Double getMoyenneByStudent(Long studentId);

    // Gestion des absences
    List<AbsenceDTO> getAbsencesByStudent(Long studentId);

    // Gestion des évaluations
    List<EvaluationDTO> getEvaluationsByStudent(Long studentId);

    // Gestion de la messagerie
    List<MessageDTO> getMessagesByParent(Long parentId);
    List<MessageDTO> getConversationWithTeacher(Long parentId, Long teacherId);
    MessageDTO sendMessageToTeacher(Long parentId, Long teacherId, String message);

    // Gestion des rendez-vous
    List<RdvDTO> getRdvsByParent(Long parentId);
    RdvDTO demanderRdv(Long studentId, Long teacherId, LocalDate date, LocalTime heureDebut, LocalTime heureFin, String motif);
    void annulerRdv(Long rdvId);

    // Gestion des professeurs
    List<TeacherDTO> getTeachersByStudent(Long studentId);
}
