package ci.ada.monetabv2new.services.facades;



import java.time.Instant;
import java.util.List;


import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.models.enumeration.Role;
import ci.ada.monetabv2new.models.enumeration.TypeAbsence;
import ci.ada.monetabv2new.services.*;
import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.impl.UserAccountService;
import ci.ada.monetabv2new.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherFacadeImpl implements TeacherFacade {

    private final ClassService classService;
    private final StudentService studentService;
    private final EvaluationService evaluationService;
    private final UserAccountService userAccountService;
    private final TeacherService teacherService;
    private final ModelMapper modelMapper;
    private final NoteService noteService;
    private final CoursService coursService;
    private final AbsenceService absenceService;
    private final MatiereService matiereService;
    private final MessageService messageService;
    private final ParentService parentService;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<ClassDTO> getClassesByTeacher(Long teacherId) {
        return classService.getClassesByTeacherId(teacherId);
    }

    @Override
    public List<StudentDTO> getStudentsByClass(Long classId) {
        return studentService.getStudentsByClassId(classId);
    }

    @Override
    public List<EvaluationDTO> getEvaluationsByClass(Long classId) {
        return evaluationService.getByClasse(classId);


    }

    @Override
    public List<NoteDTO> getNotesByStudent(Long studentId) {
        return noteService.getNotesByStudent(studentId);
    }

    @Override
    public List<NoteDTO> getNotesByEvaluationId(Long evaluationId) {
        return noteService.getNotesByEvaluation(evaluationId);
    }

    @Override
    public StudentDTO addStudentToClass(Long studentId, Long classId) {
        return null;

    }

    @Override
    public TeacherDTO findByLogin(String login) {
        Optional<UserAccountDTO> account = userAccountService.findByLogin(login);
        return teacherService.findByLogin(modelMapper.map(account, UserAccountEntity.class)).orElse(null);
    }

    @Override
    public void addEvaluation(EvaluationDTO evaluationDTO) {

        evaluationService.save(evaluationDTO);
    }

    @Override
    public void saveNotes(List<NoteDTO> notes) {

    }


    @Override
    public void saveNote(NoteDTO note) {
        noteService.save(note);
    }

    @Override
    public void removeStudentFromClass(Long studentId, Long classId) {
        /*studentService.removeStudentFromClass(studentId, classId);

         */
    }

    @Override
    public List<CoursDTO> getCoursByClasseAndTeacher(Long classeId, Long teacherId) {
        return coursService.findByClassAndTeacher(classeId, teacherId);
    }

    @Override
    public CoursDTO getCoursById(Long coursId) {
        return coursService.getById(coursId);
    }

    @Override
    public List<AbsenceDTO> getAbsencesByCours(Long coursId) {
        CoursDTO cours = new CoursDTO();
        cours.setId(coursId);
        return absenceService.getAllByCoursId(cours);
    }

    @Override
    public void saveAbsences(Long coursId, List<Long> presentStudents, Map<Long, Boolean> justifieeMap, Map<Long, String> motifMap, Map<Long, TypeAbsence> typeAbsenceMap) {
        CoursDTO cours = coursService.getById(coursId);
        List<StudentDTO> students = studentService.getStudentsByClassId(cours.getClasse().getId());

        for (StudentDTO student : students) {
            boolean isPresent = presentStudents != null && presentStudents.contains(student.getId());

            if (!isPresent) {
                AbsenceDTO absence = new AbsenceDTO();
                absence.setEtudiant(student);
                absence.setCours(cours);
                absence.setJustifiee(justifieeMap != null ? justifieeMap.getOrDefault(student.getId(), false) : false);
                absence.setMotif(motifMap != null ? motifMap.get(student.getId()) : null);
                absence.setTypeAbsence(typeAbsenceMap != null ? typeAbsenceMap.get(student.getId()) : TypeAbsence.ABSENCE);

                absenceService.save(absence);
            } else {

                CoursDTO coursDTO = new CoursDTO();
                coursDTO.setId(coursId);
                absenceService.deleteByCoursAndStudent(coursDTO, student);
            }
        }
    }

    @Override
    public ClassDTO getClassById(Long classeId) {
        return classService.getById(classeId);
    }

    @Override
    public TeacherDTO partialUpdate(TeacherDTO teacherDTO) {


        UserAccountDTO userAccountDTO = teacherDTO.getUserAccount();
        userAccountDTO = userAccountService.partialUpdate(userAccountDTO);
        teacherDTO.setUserAccount(userAccountDTO);

        MatiereDTO matiereDTO = matiereService.getById(teacherDTO.getMatiere().getId());
        teacherDTO.setMatiere(matiereDTO);

        return teacherService.partialUpdate(teacherDTO);
    }

    @Override
    public TeacherDTO save(TeacherDTO teacherDTO) {
        UserAccountDTO userAccountDTO = teacherDTO.getUserAccount();
        String rawPassword = PasswordUtils.generatePasswordFromBirthDate(teacherDTO.getBirthDate(), teacherDTO.getNom(), teacherDTO.getPrenom());
        userAccountDTO.setRole(Role.TEACHER);


        teacherDTO.getUserAccount().setPassword(passwordEncoder.encode(rawPassword));


        userAccountDTO = userAccountService.save(userAccountDTO);
        teacherDTO.setUserAccount(userAccountDTO);

        MatiereDTO matiereDTO = matiereService.getById(teacherDTO.getMatiere().getId());
        teacherDTO.setMatiere(matiereDTO);

        TeacherDTO savedTeacher = teacherService.save(teacherDTO);
        if (savedTeacher.getUserAccount().getPassword()!=null){
            emailService.sendTeacherConfirmation(
                    teacherDTO.getEmail(),
                    teacherDTO.getNom(),
                    teacherDTO.getPrenom(),
                    teacherDTO.getLogin(),
                    rawPassword,
                    teacherDTO.getMatiere().getNom()
            );
        }else{
            throw new IllegalArgumentException("Le mot de passe n'a pas été généré");
        }

        return savedTeacher;
    }

    @Override
    public List<MessageDTO> getMessagesByTeacher(Long teacherId) {
        UserAccountDTO teacherAccount = teacherService.getById(teacherId).getUserAccount();
        return messageService.getMessagesByUser(teacherAccount);

    }


    @Override
    public List<MessageDTO> getConversationWithParent(Long parentId, Long teacherId) {
        UserAccountDTO parentAccount = parentService.getById(parentId).getUserAccount();
        UserAccountDTO teacherAccount = teacherService.getById(teacherId).getUserAccount();
        return messageService.getConversationBetweenUsers(parentAccount, teacherAccount);
    }

    @Override
    public MessageDTO sendMessageToParent(Long parentId, Long teacherId, String message) {
        UserAccountDTO parentAccount = parentService.getById(parentId).getUserAccount();
        UserAccountDTO teacherAccount = teacherService.getById(teacherId).getUserAccount();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender(parentAccount);
        messageDTO.setReceiver(teacherAccount);
        messageDTO.setMessage(message);
        messageDTO.setDate(Instant.now());

        return messageService.save(messageDTO);
    }
}
