package ci.ada.monetabv2new.services.facades;


import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.models.enumeration.Role;
import ci.ada.monetabv2new.services.*;
import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.impl.UserAccountService;
import ci.ada.monetabv2new.services.mapper.StudentMapper;
import ci.ada.monetabv2new.utils.MatriculeGenerator;
import ci.ada.monetabv2new.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentFacadeImpl implements StudentFacade {
    private final StudentService studentService;
    private final AbsenceService absenceService;
    private final UserAccountService userAccountService;
    private final EvaluationService evaluationService;
    private final CoursService coursService;
    private final NoteService noteService;
    private final ClassService classService;
    private final TeacherService teacherService;

    private final StudentMapper studentMapper;
    private final ModelMapper modelMapper;

    private final PasswordUtils passwordUtils;
    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AbsenceDTO> getStudentAbsences(StudentDTO student) {
        return absenceService.getAllByStudentId(student);
    }

    @Override
    public StudentDTO saveStudent(StudentEntity student) {
        String rawPassword = generateInitialPassword(
                student.getNom(),
                student.getPrenom(),
                student.getBirthDate()
        );
        student.getUserAccountEntity().setPassword(rawPassword);

        student.setUserAccountEntity(
                modelMapper.map(
                        userAccountService.save(
                            modelMapper.map(student.getUserAccountEntity(),
                             UserAccountDTO.class)
                ), UserAccountEntity.class)
        );

        student.setMatricule(MatriculeGenerator.generateForCurrentYear("ETAB"));


        return studentService.save(studentMapper.toDto(student));
    }


    @Override
    public Optional<StudentDTO> getStudentByLogin(String login) {

        Optional<UserAccountDTO> account = userAccountService.findByLogin(login);
        return studentService.findByLogin(modelMapper.map(account, UserAccountEntity.class));
    }

    @Override
    public List<EvaluationDTO> getStudentEvaluations(StudentDTO student) {
        return evaluationService.getByStudent(student.getId());
    }

    @Override
    public List<CoursDTO> getStudentCours(StudentDTO student) {
        return coursService.findByClassId(student.getClassDTO().getId());
    }

    @Override
    public List<NoteDTO> getStudentNotes(StudentDTO student) {
        return noteService.getNotesByStudent(student.getId());
    }

    @Override
    public StudentDataDTO getStudentData(StudentDTO student) {
        StudentDataDTO studentDataDTO = new StudentDataDTO();
        studentDataDTO.setStudent(student);
        studentDataDTO.setNotes(getStudentNotes(student));
        studentDataDTO.setMoyenne(calculerMoyenneGenerale(student, getClassMatieres(student.getClassDTO())));
        studentDataDTO.setAbsences(getStudentAbsences(student));
        studentDataDTO.setAssiduite((double) getStudentAbsences(student).size()*100/getStudentCours(student).size());
        studentDataDTO.setEvaluations(getStudentEvaluations(student));
        studentDataDTO.setCours(getStudentCours(student));

        return studentDataDTO;
    }

    @Override
    public StudentDTO partialUpdate(StudentDTO studentDTO) {

        UserAccountDTO userAccountDTO = studentDTO.getUserAccount();
        userAccountDTO = userAccountService.partialUpdate(userAccountDTO);
        studentDTO.setUserAccount(userAccountDTO);

        ClassDTO classDTO = studentDTO.getClassDTO();
        classDTO = classService.partialUpdate(classDTO);
        studentDTO.setClassDTO(classDTO);

        return studentService.partialUpdate(studentDTO);
    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        UserAccountDTO userAccountDTO = studentDTO.getUserAccount();
        String rawPassword = PasswordUtils.generatePasswordFromBirthDate(studentDTO.getBirthDate(), studentDTO.getNom(), studentDTO.getPrenom());


        studentDTO.getUserAccount().setPassword(passwordEncoder.encode(rawPassword));

        userAccountDTO = userAccountService.save(userAccountDTO);
        studentDTO.setUserAccount(userAccountDTO);

        ClassDTO classDTO = studentDTO.getClassDTO();
        classDTO = classService.getById(classDTO.getId());
        studentDTO.setClassDTO(classDTO);
        studentDTO.setMatricule(MatriculeGenerator.generateForCurrentYear("ETAB"));
        studentDTO.getUserAccount().setRole(Role.STUDENT);

        StudentDTO savedStudent = studentService.save(studentDTO);
        if (savedStudent.getUserAccount().getPassword()!=null){
            emailService.sendStudentConfirmation(
                    studentDTO.getEmail(),
                    studentDTO.getNom(),
                    studentDTO.getPrenom(),
                    studentDTO.getLogin(),
                    rawPassword,
                    studentDTO.getClassDTO().getLabel(),
                    studentDTO.getMatricule()
            );
        }else{
            throw new IllegalArgumentException("Le mot de passe n'a pas été généré");
        }

        return savedStudent;
    }

    private List<MatiereDTO> getClassMatieres(ClassDTO classDTO){

        return teacherService.getTeachersByClassId(classDTO.getId()).stream().map(
                TeacherDTO::getMatiere
        ).toList();


    }

    private double calculerMoyenneParMatiere(StudentDTO student, MatiereDTO matiere) {
        List<NoteDTO> notesMatiere = getStudentNotes(student).stream()
                .filter(note -> note.getEvaluation() != null
                        && note.getEnseignant() != null
                        && note.getEnseignant().getMatiere() != null
                        && note.getEnseignant().getMatiere().getId().equals(matiere.getId())
                        && note.getValeur() != null)
                .toList();

        if (notesMatiere.isEmpty()) {
            return 0.0;
        }

        // Moyenne pondérée par les coefficients
        double total = notesMatiere.stream()
                .mapToDouble(n -> n.getValeur() * (n.getCoefficient() != null ? n.getCoefficient() : 1.0))
                .sum();

        double sommeCoeffs = notesMatiere.stream()
                .mapToDouble(n -> (n.getCoefficient() != null ? n.getCoefficient() : 1.0))
                .sum();

        return sommeCoeffs > 0 ? total / sommeCoeffs : 0.0;
    }


    private double calculerMoyenneGenerale(StudentDTO student, List<MatiereDTO> matieres) {
        List<Double> moyennesMatieres = matieres.stream()
                .map(matiere -> calculerMoyenneParMatiere(student, matiere))
                .filter(moyenne -> moyenne > 0) // ignorer les matières sans notes
                .toList();

        if (moyennesMatieres.isEmpty()) {
            return 0.0;
        }

        return moyennesMatieres.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public String generateInitialPassword(String firstName, String lastName, java.time.LocalDate birthDate) {
        return firstName.substring(0, 2).toLowerCase()
                + lastName.substring(0, 2).toLowerCase()
                + birthDate.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyyyy"));
    }
}
