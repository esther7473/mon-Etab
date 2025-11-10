package ci.ada.monetabv2new.controllers;



import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.ClassService;
import ci.ada.monetabv2new.services.MatiereService;
import ci.ada.monetabv2new.services.StudentService;
import ci.ada.monetabv2new.services.TeacherService;
import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.facades.AdminFacade;
import ci.ada.monetabv2new.services.facades.StudentFacade;
import ci.ada.monetabv2new.services.facades.TeacherFacade;
import ci.ada.monetabv2new.services.impl.UserAccountService;
import ci.ada.monetabv2new.services.mapper.ClassMapper;
import ci.ada.monetabv2new.services.mapper.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final TeacherService teacherService;
    private final ClassService classService;
    private final TeacherFacade teacherFacade;
    private final TeacherMapper teacherMapper;
    private final ClassMapper classMapper;
    private final MatiereService matiereService;
    private final StudentService studentService;
    private final UserAccountService userAccountService;

    private final AdminFacade adminFacade;
    private final StudentFacade studentFacade;
    private final ModelMapper modelMapper;


    public AdminDTO getUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        Optional<AdminDTO> studentOptional = Optional.ofNullable(adminFacade.getByLogin(login));
        if (studentOptional.isEmpty()) {

            throw new RuntimeException("account not found for authenticated user: " + login);
        }
        if(studentOptional.get() == null) {
            studentOptional.get().setNom("KONE");
            studentOptional.get().setPrenom("SIRIKY");
            studentOptional.get().setUserAccountEntity(
                    modelMapper.map(
                            userAccountService.partialUpdate(
                                    modelMapper.map(studentOptional.get().getUserAccountEntity(), UserAccountDTO.class)
                            ), UserAccountEntity.class)
            );
        }
        return studentOptional.get();


    }


    @GetMapping("/")
    public String home(Model model) {
        AdminDTO admin = getUserAdmin();
        model.addAttribute("admin", admin);

        long nombreEleves = adminFacade.getStudents().size();
        long nombreTeachers = adminFacade.getTeachers().size();
        long nombreHeuresCours = adminFacade.getCours().size();
        long nombreClasses = adminFacade.getClasses().size();
        long nombreMatieres = adminFacade.getMatieres().size();

        Map<String, Long> repartitionEleves = adminFacade.getStudents().stream()
                .collect(Collectors.groupingBy(
                        student -> student.getClassDTO() != null ? student.getClassDTO().getLabel() : "Non assigné",
                        Collectors.counting()
                ));

        model.addAttribute("nombreEleves", nombreEleves);
        model.addAttribute("nombreTeachers", nombreTeachers);
        model.addAttribute("nombreHeuresCours", nombreHeuresCours);
        model.addAttribute("nombreClasses", nombreClasses);
        model.addAttribute("nombreMatieres", nombreMatieres);
        model.addAttribute("repartitionEleves", repartitionEleves);

        return "admin/home";
    }

    @GetMapping("/enseignants")
    public String enseignants(Model model) {
        List<TeacherDTO> teachers = adminFacade.getTeachers();
        System.out.println("Number of teachers found: " + teachers.size());
        model.addAttribute("teachers", teachers);
        return "admin/teacher";
    }

    @GetMapping("/enseignants/add")
    public String showAddTeacherForm(Model model) {
        TeacherDTO teacher = new TeacherDTO();
        model.addAttribute("teacher", teacher);
        model.addAttribute("matieres", adminFacade.getMatieres());
        return "admin/teacherForm";
    }

    @PostMapping("/enseignants/add")
    public String saveTeacher(@ModelAttribute("teacher") TeacherDTO teacher) {
        teacherFacade.save(teacher);
        return "redirect:/admin/enseignants";
    }
    
    @GetMapping("/enseignants/edit/{id}")
    public String showEditTeacherForm(@PathVariable Long id, Model model) {
        TeacherDTO teacher = teacherService.getById(id);

        model.addAttribute("teacher", teacher);
        model.addAttribute("matieres", adminFacade.getMatieres());
        return "admin/teacherForm";
    }
    
    @PostMapping("/enseignants/update/{id}")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute("teacher") TeacherDTO teacher) {
        teacher.setId(id);
        teacherFacade.partialUpdate(teacher);
        return "redirect:/admin/enseignants";
    }

    @PostMapping("/enseignants/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);
        return "redirect:/admin/enseignants";
    }

    // Matiere management routes
    @GetMapping("/matieres")
    public String matieres(Model model) {
        model.addAttribute("matieres", adminFacade.getMatieres());
        return "admin/matiere";
    }

    @GetMapping("/matieres/add")
    public String showAddMatiereForm(Model model) {
        model.addAttribute("matiere", new MatiereDTO());
        return "admin/matiereForm";
    }

    @PostMapping("/matieres/save")
    public String saveMatiere(@ModelAttribute("matiere") MatiereDTO matiere) {
        adminFacade.addMatiere(matiere);
        return "redirect:/admin/matieres";
    }

    @GetMapping("/matieres/edit/{id}")
    public String showEditMatiereForm(@PathVariable Long id, Model model) {
        model.addAttribute("matiere", matiereService.getById(id));
        return "admin/matiereForm";
    }

    @PostMapping("/matieres/update/{id}")
    public String updateMatiere(@PathVariable Long id, @ModelAttribute("teacher") MatiereDTO matiereDTO) {
        matiereDTO.setId(id);
        matiereService.partialUpdate(matiereDTO);
        return "redirect:/admin/matieres";
    }
    @PostMapping("/matieres/delete/{id}")
    public String deleteMatiere(@PathVariable Long id) {
        matiereService.delete(id);
        return "redirect:/admin/matieres";
    }

    // Class management routes
    @GetMapping("/classes")
    public String classes(Model model) {
        model.addAttribute("classes", classService.getAll());
        return "admin/class";
    }

    @GetMapping("/classes/add")
    public String showAddClassForm(Model model) {
        model.addAttribute("classe", new ClassDTO());
        return "admin/classForm";
    }

    @PostMapping("/classes/save")
    public String saveClass(@ModelAttribute("classe") ClassDTO classe) {
        classService.save(classe);
        return "redirect:/admin/classes";
    }

    @GetMapping("/classes/edit/{id}")
    public String showEditClassForm(@PathVariable Long id, Model model) {
        model.addAttribute("classe", classService.getById(id));
        return "admin/classForm";
    }

    @PostMapping("/classes/delete/{id}")
    public String deleteClass(@PathVariable Long id) {
        classService.delete(id);
        return "redirect:/admin/classes";
    }

    // Students management routes
    @GetMapping("/eleves")
    public String student(Model model) {
        List<StudentDTO> students = adminFacade.getStudents();
        model.addAttribute("students", students);

        AdminDTO admin = getUserAdmin();
        model.addAttribute("admin", admin);
        return "admin/student";
    }
    @GetMapping("/eleves/edit/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getById(id));
        model.addAttribute("classes", adminFacade.getClasses());

        return "admin/studentForm";
    }
    @GetMapping("/eleves/add")
    public String showAddStudentForm( @ModelAttribute("student") StudentDTO student, Model model) {
        model.addAttribute("student", student);
        model.addAttribute("classes", adminFacade.getClasses());

        AdminDTO admin = getUserAdmin();
        model.addAttribute("admin", admin);
        return "admin/studentAddForm";
    }
    @PostMapping("/eleves/save")
    public String saveStudent( @ModelAttribute("student") StudentDTO student) {
        studentFacade.save(student);
        return  "redirect:/admin/eleves";
    }

    @PostMapping("/eleves/edit")
    public String editStudent(@ModelAttribute("student") StudentDTO student ) {
        studentFacade.partialUpdate(student);
        return  "redirect:/admin/eleves";
    }
    // Parameters route
    @GetMapping("/parametres")
    public String parametres() {
        return "admin/parameters";
    }
}
