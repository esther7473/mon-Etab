package ci.ada.monetabv2new.services.facades;


import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.models.enumeration.Statut;
import ci.ada.monetabv2new.services.*;
import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.impl.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentFacadeImpl implements ParentFacade {

    private final ParentService parentService;
    private final UserAccountService userAccountService;
    private final StudentService studentService;
    private final NoteService noteService;
    private final AbsenceService absenceService;
    private final EvaluationService evaluationService;
    private final MessageService messageService;
    private final RdvService rdvService;
    private final TeacherService teacherService;
    private final ModelMapper modelMapper;

    @Override
    public ParentDTO getParent(String login) {
        Optional<UserAccountDTO> account = userAccountService.findByLogin(login);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found for login: " + login);
        }
        return parentService.getByLogin(modelMapper.map(account.get(), UserAccountEntity.class));
    }

    @Override
    public List<StudentDTO> getChildrenByParent(Long parentId) {
        return studentService.getStudentsByParentId(parentId);
    }

    @Override
    public List<NoteDTO> getNotesByStudent(Long studentId) {
        return noteService.getNotesByStudent(studentId);
    }

    @Override
    public List<AbsenceDTO> getAbsencesByStudent(Long studentId) {
        StudentDTO student = studentService.getById(studentId);
        return absenceService.getAllByStudentId(student);
    }

    @Override
    public List<EvaluationDTO> getEvaluationsByStudent(Long studentId) {
        // Récupérer l'étudiant pour avoir sa classe
        StudentDTO student = studentService.getById(studentId);
        if (student != null && student.getClassDTO() != null) {
            return evaluationService.getByClasse(student.getClassDTO().getId());
        }
        return List.of();
    }

    @Override
    public Double getMoyenneByStudent(Long studentId) {
        List<NoteDTO> notes = noteService.getNotesByStudent(studentId);
        if (notes.isEmpty()) {
            return 0.0;
        }

        double somme = notes.stream()
                .mapToDouble(NoteDTO::getValeur)
                .sum();
        return somme / notes.size();
    }

    @Override
    public List<MessageDTO> getMessagesByParent(Long parentId) {
        UserAccountDTO parentAccount = parentService.getById(parentId).getUserAccount();
        return messageService.getMessagesByUser(parentAccount);
    }

    @Override
    public List<MessageDTO> getConversationWithTeacher(Long parentId, Long teacherId) {
        UserAccountDTO parentAccount = parentService.getById(parentId).getUserAccount();
        UserAccountDTO teacherAccount = teacherService.getById(teacherId).getUserAccount();
        return messageService.getConversationBetweenUsers(parentAccount, teacherAccount);
    }

    @Override
    public MessageDTO sendMessageToTeacher(Long parentId, Long teacherId, String message) {
        UserAccountDTO parentAccount = parentService.getById(parentId).getUserAccount();
        UserAccountDTO teacherAccount = teacherService.getById(teacherId).getUserAccount();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSender(parentAccount);
        messageDTO.setReceiver(teacherAccount);
        messageDTO.setMessage(message);
        messageDTO.setDate(Instant.now());

        return messageService.save(messageDTO);
    }

    @Override
    public List<RdvDTO> getRdvsByParent(Long parentId) {
        List<StudentDTO> children = getChildrenByParent(parentId);
        return children.stream()
                .flatMap(student -> rdvService.getRdvsByStudent(student.getId()).stream())
                .collect(Collectors.toList());
    }

    @Override
    public RdvDTO demanderRdv(Long studentId, Long teacherId, LocalDate date, LocalTime heureDebut, LocalTime heureFin, String motif) {
        StudentDTO student = studentService.getById(studentId);
        TeacherDTO teacher = teacherService.getById(teacherId);

        RdvDTO rdvDTO = new RdvDTO();
        rdvDTO.setStudent(student);
        rdvDTO.setTeacher(teacher);
        rdvDTO.setDate(date);
        rdvDTO.setHeureDebut(heureDebut);
        rdvDTO.setHeureFin(heureFin);
        rdvDTO.setMotif(motif);
        rdvDTO.setStatut(Statut.EN_ATTENTE); // Assuming EN_ATTENTE is a status

        return rdvService.save(rdvDTO);
    }

    @Override
    public void annulerRdv(Long rdvId) {
        RdvDTO rdv = rdvService.getById(rdvId);
        rdv.setStatut(Statut.ANNULE);
        rdvService.save(rdv);
    }

    @Override
    public List<TeacherDTO> getTeachersByStudent(Long studentId) {
        StudentDTO student = studentService.getById(studentId);
        if (student != null && student.getClassDTO() != null) {
            return teacherService.getTeachersByClassId(student.getClassDTO().getId());
        }
        return List.of();
    }

    @Override
    public StudentDTO getStudentById(Long studentId, Long parentId) {
        // Vérifier que l'étudiant appartient bien au parent
        List<StudentDTO> children = getChildrenByParent(parentId);
        return children.stream()
                .filter(student -> student.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Student not found or not belonging to this parent"));
    }

    @Override
    public ParentDTO partialUpdate(ParentDTO parentDTO) {
        UserAccountDTO userAccountDTO = parentDTO.getUserAccount();
        userAccountDTO = userAccountService.partialUpdate(userAccountDTO);
        parentDTO.setUserAccount(userAccountDTO);

        return parentService.partialUpdate(parentDTO);
    }
}
