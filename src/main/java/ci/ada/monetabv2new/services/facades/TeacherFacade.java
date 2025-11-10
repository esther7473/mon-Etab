package ci.ada.monetabv2new.services.facades;


import ci.ada.monetabv2new.models.enumeration.TypeAbsence;
import ci.ada.monetabv2new.services.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TeacherFacade {

    /**
     * Liste toutes les classes de l'enseignant
     */
    List<ClassDTO> getClassesByTeacher(Long teacherId);

    /**
     * Récupère tous les élèves d'une classe donnée
     */
    List<StudentDTO> getStudentsByClass(Long classId);

    /**
     * Liste toutes les évaluations d'une classe
     */
    List<EvaluationDTO> getEvaluationsByClass(Long classId);

    /**
     * Récupère les notes d'un élève pour toutes ses évaluations
     */
    List<NoteDTO> getNotesByStudent(Long studentId);

    List<NoteDTO> getNotesByEvaluationId(Long evaluationId);

    /**
     * Ajouter un nouvel élève dans une classe
     */
    StudentDTO addStudentToClass(Long studentId, Long classId);

    TeacherDTO findByLogin(String login);

    void addEvaluation(EvaluationDTO evaluationDTO);

    void saveNotes(List<NoteDTO> notes);

    void saveNote(NoteDTO note);

    /**
     * Supprimer un élève d'une classe
     */
    void removeStudentFromClass(Long studentId, Long classId);

    // Méthodes pour les absences
    List<CoursDTO> getCoursByClasseAndTeacher(Long classeId, Long teacherId);
    CoursDTO getCoursById(Long coursId);
    List<AbsenceDTO> getAbsencesByCours(Long coursId);
    void saveAbsences(Long coursId, List<Long> presentStudents,
                      Map<Long, Boolean> justifieeMap,
                      Map<Long, String> motifMap,
                      Map<Long, TypeAbsence> typeAbsenceMap);

    // Méthode utilitaire
    ClassDTO getClassById(Long classeId);

    TeacherDTO partialUpdate(TeacherDTO teacherDTO);

    TeacherDTO save(TeacherDTO teacherDTO);


    //=========MESSAGES==========
    List<MessageDTO> getMessagesByTeacher(Long teacherId);
    List<MessageDTO> getConversationWithParent(Long parentId, Long teacherId);

    MessageDTO sendMessageToParent(Long parentId, Long teacherId, String message);

}
