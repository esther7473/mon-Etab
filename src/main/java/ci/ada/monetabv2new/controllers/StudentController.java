package ci.ada.monetabv2new.controllers;

import ci.ada.monetabv2new.services.StudentService;
import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.facades.StudentFacade;
import ci.ada.monetabv2new.utils.TimeTableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/eleve")
public class StudentController {

    private final StudentService studentService;
    private final StudentFacade studentFacade;
    private final TimeTableUtils timeTableUtils;


    public StudentDTO addStudentToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        Optional<StudentDTO> studentOptional = studentFacade.getStudentByLogin(login);
        if (studentOptional.isEmpty()) {

            throw new RuntimeException("Student not found for authenticated user: " + login);
        }
        System.out.println("======DEBUG STUDENTS=========="+studentOptional.get().getUserAccount().getUsername());
        return studentOptional.get();


    }

    @ModelAttribute("studentData")
    public StudentDataDTO addStudentDataToModel(Model model) {
        StudentDTO student = addStudentToModel();

        return  studentFacade.getStudentData(student);
    }

    @GetMapping("/")
    public String studentDashboard(Model model) {
        StudentDTO student = addStudentToModel();
        model.addAttribute("student", student);
        model.addAttribute("studentData", studentFacade.getStudentData(student));
        return "eleve/home";
    }

    @GetMapping("/notes")
    public String studentNotes(Model model) {
        StudentDTO student = addStudentToModel();
        model.addAttribute("student", student);

        List<NoteDTO> notes = studentFacade.getStudentNotes(student);

        // DEBUG
        System.out.println("=== DEBUG NOTES ===");
        for (NoteDTO note : notes) {
            System.out.println("Note: " + note.getValeur());
            System.out.println("Enseignant: " + (note.getEnseignant() != null ? "présent" : "null"));
            if (note.getEnseignant() != null) {
                System.out.println("Matière: " + (note.getEnseignant().getMatiere() != null
                        ? note.getEnseignant().getMatiere().getNom() : "null"));
            }
            System.out.println("---------------");
        }

        // Grouper par matière
        Map<String, List<NoteDTO>> notesParMatiere = notes.stream()
                .filter(note -> note.getValeur() != null)
                .filter(note -> note.getEnseignant() != null)
                .filter(note -> note.getEnseignant().getMatiere() != null)
                .filter(note -> note.getEnseignant().getMatiere().getNom() != null)
                .collect(Collectors.groupingBy(
                        note -> note.getEnseignant().getMatiere().getNom(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        Map<String, Double> moyennesParMatiere = new HashMap<>();
        for (Map.Entry<String, List<NoteDTO>> entry : notesParMatiere.entrySet()) {
            double moyenne = entry.getValue().stream()
                    .mapToDouble(NoteDTO::getValeur)
                    .average()
                    .orElse(0.0);
            moyennesParMatiere.put(entry.getKey(), moyenne);
        }

        double moyenneGenerale = notes.stream()
                .filter(note -> note.getValeur() != null)
                .mapToDouble(NoteDTO::getValeur)
                .average()
                .orElse(0.0);

        model.addAttribute("notes", notes);
        model.addAttribute("notesParMatiere", notesParMatiere);
        model.addAttribute("moyennesParMatiere", moyennesParMatiere);
        model.addAttribute("moyenneGenerale", moyenneGenerale);
        model.addAttribute("nombreNotes", notes.size());
        model.addAttribute("nombreMatieres", notesParMatiere.size());

        return "eleve/notes";
    }



    @GetMapping("/emploi-du-temps")
    public String studentTimetable(Model model) {
        StudentDTO student = addStudentToModel();
        model.addAttribute("student", student);
        
        List<CoursDTO> courses = studentFacade.getStudentCours(student);
        
        System.out.println("======DEBUG COURSES COUNT=======" + (courses != null ? courses.size() : "NULL"));
        
        timeTableUtils.prepareTimeTableData(courses, model);
        
        assert courses != null;
        long differentSubjects = timeTableUtils.countDifferentSubjects(courses);
        model.addAttribute("differentSubjects", differentSubjects);
        
        return "eleve/timeTable";
    }

    @GetMapping("/devoirs")
    public String studentHomeworks(Model model) {
        StudentDTO student = addStudentToModel();
        model.addAttribute("student", student);

        List<EvaluationDTO> evaluations= studentFacade.getStudentEvaluations(student);
        model.addAttribute("evaluations", evaluations);

        return "eleve/devoir";
    }

    @GetMapping("/absences")
    public String studentAbsences(Model model) {
        StudentDTO student = addStudentToModel();
        model.addAttribute("student", student);

        List<AbsenceDTO> absences = studentFacade.getStudentAbsences(student);
        model.addAttribute("absences", absences);
        return "eleve/absence";
    }

    @GetMapping("/justificatifs")
    public String studentJustifications(Model model) {
        StudentDTO student = (StudentDTO) model.getAttribute("student");
        return "eleve/justif";
    }

    @GetMapping("/profil")
    public String studentProfile(Model model) {
        StudentDTO student = addStudentToModel();
        model.addAttribute("student", student);
        System.out.println("Student classe: " + student.getClassDTO().getLabel());
        return "eleve/profil";
    }

    @PostMapping("/profil/update")
    public String updateProfile(@ModelAttribute("student") StudentDTO student,
                                RedirectAttributes redirectAttributes) {
        try {
            StudentDTO requestDTO = new StudentDTO();
            requestDTO.setId(student.getId());
            requestDTO.setNom(student.getNom());
            requestDTO.setPrenom(student.getPrenom());
            requestDTO.setTel(student.getTel());
            requestDTO.setPays(student.getPays());
            if (student.getClassDTO().getId() != null) {
                ClassDTO classDTO = new ClassDTO();
                classDTO.setId(student.getClassDTO().getId());
                classDTO.setLabel(student.getClassDTO().getLabel());
                requestDTO.setClassDTO(classDTO);
            }

            if (student.getUserAccount() != null) {
                UserAccountDTO userAccountDTO = new UserAccountDTO();
                userAccountDTO.setId(student.getUserAccount().getId());
                userAccountDTO.setUsername(student.getUserAccount().getUsername());
                userAccountDTO.setPassword(student.getUserAccount().getPassword());
                userAccountDTO.setRole(student.getUserAccount().getRole());

                requestDTO.setUserAccount(userAccountDTO);
            }
            
            studentFacade.partialUpdate(requestDTO);
            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du profil: " + e.getMessage());
        }
        return "redirect:/eleve/profil";
    }



}