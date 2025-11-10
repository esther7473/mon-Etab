package ci.ada.monetabv2new.services.facades;


import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.*;
import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.impl.UserAccountService;
import ci.ada.monetabv2new.services.mapper.ClassMapper;
import ci.ada.monetabv2new.services.mapper.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminFacadeImpl implements AdminFacade {
    private final TeacherService teacherService;
    private final ClassService classService;
    private final TeacherFacade teacherFacade;
    private final TeacherMapper teacherMapper;
    private final ClassMapper classMapper;
    private final UserAccountService userAccountService;
    private final CoursService coursService;
    private final EvaluationService evaluationService;
    private final AbsenceService absenceService;
    private final StudentService studentService;
    private final NoteService noteService;
    private final MessageService messageService;
    private final MatiereService matiereService;
    private final AdminService adminService;
    private final ModelMapper modelMapper;


    @Override
    public List<TeacherDTO> getTeachers() {
        return teacherService.getAll();
    }

    @Override
    public List<StudentDTO> getStudents() {
        return studentService.getAll();
    }

    @Override
    public List<MatiereDTO> getMatieres() {
        return matiereService.getAll();
    }

    @Override
    public List<ClassDTO> getClasses() {
        return classService.getAll();
    }

    @Override
    public List<EvaluationDTO> getEvaluations() {
        return List.of();
    }

    @Override
    public List<AbsenceDTO> getAbsences() {
        return List.of();
    }

    @Override
    public List<CoursDTO> getCours() {
        return coursService.getAll();
    }

    @Override
    public void addTeacher(TeacherDTO teacher) {
        teacherService.save(teacher);
    }

    @Override
    public void addStudent(StudentDTO student) {
        studentService.save(student);
    }

    @Override
    public void addMatiere(MatiereDTO matiere) {
        matiereService.save(matiere);
    }

    @Override
    public void addClass(ClassDTO classDTO) {
        classService.save(classDTO);
    }

    @Override
    public void addEvaluation(EvaluationDTO evaluation) {

    }

    @Override
    public void addAbsence(AbsenceDTO absence) {

    }

    @Override
    public void addCours(CoursDTO cours) {
        coursService.save(cours);
    }

    @Override
    public Optional<UserAccountDTO> getUserAdmin(String login) {
        return userAccountService.findByLogin(login);
    }

    @Override
    public AdminDTO getByLogin(String login) {
        Optional<UserAccountDTO> account = userAccountService.findByLogin(login);
        return adminService.getByLogin(modelMapper.map(account.get(), UserAccountEntity.class));
    }
}
