package ci.ada.monetabv2new.services.facades;
import ci.ada.monetabv2new.services.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface AdminFacade {

    List<TeacherDTO> getTeachers();
    List<StudentDTO>getStudents();
    List<MatiereDTO>getMatieres();
    List<ClassDTO>getClasses();
    List<EvaluationDTO>getEvaluations();
    List<AbsenceDTO>getAbsences();
    List<CoursDTO>getCours();

    void addTeacher(TeacherDTO teacher);
    void addStudent(StudentDTO student);
    void addMatiere(MatiereDTO matiere);
    void addClass(ClassDTO classDTO);
    void addEvaluation(EvaluationDTO evaluation);
    void addAbsence(AbsenceDTO absence);
    void addCours(CoursDTO cours);

    Optional<UserAccountDTO> getUserAdmin(String login);

    AdminDTO getByLogin(String login);

}
