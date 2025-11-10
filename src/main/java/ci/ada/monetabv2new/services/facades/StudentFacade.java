package ci.ada.monetabv2new.services.facades;

import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.services.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentFacade {

    public List<AbsenceDTO> getStudentAbsences(StudentDTO student);

    public StudentDTO saveStudent(StudentEntity student);

    public Optional<StudentDTO> getStudentByLogin(String login);

    public List<EvaluationDTO> getStudentEvaluations(StudentDTO student);

    public List<CoursDTO> getStudentCours(StudentDTO student);

    public List<NoteDTO> getStudentNotes(StudentDTO student);

    public StudentDataDTO getStudentData(StudentDTO student);

    public StudentDTO partialUpdate(StudentDTO studentDTO);

    public StudentDTO save(StudentDTO studentDTO);
    public String generateInitialPassword(String firstName, String lastName, java.time.LocalDate birthDate);
}
