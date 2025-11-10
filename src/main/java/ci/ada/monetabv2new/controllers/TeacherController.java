package ci.ada.monetabv2new.controllers;

import ci.ada.monetabv2new.models.enumeration.TypeAbsence;
import ci.ada.monetabv2new.services.CoursService;
import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.facades.TeacherFacade;
import ci.ada.monetabv2new.utils.TimeTableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherFacade teacherFacade;
    private final CoursService courseService;
    private final TimeTableUtils timeTableUtils;




    private String getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }


    @GetMapping("/")
    public String dashboard(Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);
        List<ClassDTO> classes = teacherFacade.getClassesByTeacher(teacher.getId());
        int classesNumber = teacherFacade.getClassesByTeacher(teacher.getId()).size();
        int evaluationNumber = teacherFacade.getEvaluationsByClass(teacher.getId()).size();
        int studentsNumber = 0;
        for (ClassDTO classe : classes) {
            studentsNumber += classe.getStudents().size();
        }


        model.addAttribute("teacher", teacher);
        model.addAttribute("classesNumber", classesNumber);
        model.addAttribute("evaluationNumber", evaluationNumber);
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("studentsNumber", studentsNumber);

        return "teacher/home"; 
    }



    @GetMapping("/emploi-du-temps")
    public String timeTable(Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);

        model.addAttribute("teacher", teacher);

        List<CoursDTO> courses = courseService.findByTeacherId(teacher.getId());

        List<CoursDTO> sortedCourses = courses.stream()
                .sorted(Comparator
                        .comparing(CoursDTO::getDateCours) 
                        .thenComparing(CoursDTO::getHeureDebut) 
                )
                .collect(Collectors.toList());

        long totalHours = timeTableUtils.calculateTotalHours(sortedCourses);
        long differentClasses = timeTableUtils.countDifferentClasses(sortedCourses);

   
        List<String> timeSlots = Arrays.asList(
                "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
                "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"
        );

        List<String> daysOfWeek = Arrays.asList("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi");

        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate friday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        String currentWeek = "Semaine du " + monday.format(DateTimeFormatter.ofPattern("dd MMMM")) +
                " au " + friday.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        model.addAttribute("courses", sortedCourses);
        model.addAttribute("totalHours", totalHours);
        model.addAttribute("differentClasses", differentClasses);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("currentWeek", currentWeek);



        return "teacher/timeTable";
    }

    @GetMapping("/notes")
    public String note(@RequestParam(required = false) Long classeId,
                       @RequestParam(required = false) Long evaluationId,
                       Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);

        model.addAttribute("classes", teacherFacade.getClassesByTeacher(teacher.getId()));
        model.addAttribute("teacher", teacher);

        if (classeId != null) {
            ClassDTO selectedClasse = teacherFacade.getClassesByTeacher(teacher.getId())
                    .stream()
                    .filter(c -> c.getId().equals(classeId))
                    .findFirst()
                    .orElse(null);
            System.out.println("classe: "+ Objects.requireNonNull(selectedClasse).getLabel());

            model.addAttribute("selectedClasse", selectedClasse);
            model.addAttribute("students", teacherFacade.getStudentsByClass(classeId));
            model.addAttribute("evaluations", teacherFacade.getEvaluationsByClass(classeId));

            if (evaluationId != null) {
                EvaluationDTO selectedEvaluation = teacherFacade.getEvaluationsByClass(classeId)
                        .stream()
                        .filter(e -> e.getId().equals(evaluationId))
                        .findFirst()
                        .orElse(null);

                assert selectedEvaluation != null;
                System.out.println("eval: "+selectedEvaluation.getTitre());

                model.addAttribute("selectedEvaluation", selectedEvaluation);

                if (selectedEvaluation != null) {
                    List<NoteDTO> notes = new ArrayList<>();
                    List<StudentDTO> students = teacherFacade.getStudentsByClass(classeId);

                    List<NoteDTO> existingNotes = teacherFacade.getNotesByEvaluationId(evaluationId);
                    Map<Long, NoteDTO> existingNotesMap = existingNotes.stream()
                            .collect(Collectors.toMap(n -> n.getEtudiant().getId(), n -> n));

                    for (StudentDTO student : students) {
                        NoteDTO note = existingNotesMap.getOrDefault(student.getId(), new NoteDTO());
                        note.setEtudiant(student);
                        note.setEvaluation(selectedEvaluation);
                        note.setEnseignant(teacher);
                        notes.add(note);
                    }

                    model.addAttribute("notes", notes);

                    model.addAttribute("totalStudents", students.size());
                    model.addAttribute("notesSaisies", (int) existingNotes.stream().filter(n -> n.getValeur() != null).count());
                    model.addAttribute("absents", (int) existingNotes.stream().filter(n -> n.getValeur() == null).count());

                    double moyenne = existingNotes.stream()
                            .filter(n -> n.getValeur() != null)
                            .mapToDouble(NoteDTO::getValeur)
                            .average()
                            .orElse(0.0);
                    model.addAttribute("moyenneClasse", String.format("%.1f", moyenne));
                }
            }
        }

        return "teacher/saisie-notes";
    }

    @PostMapping("/notes/save")
    public String saveNote(@ModelAttribute("note") NoteDTO note,
                           @RequestParam Long classeId) {


        teacherFacade.saveNote(note);

        return "redirect:/teacher/notes?classeId=" + classeId + "&evaluationId=" + note.getEvaluation().getId();
    }

    @GetMapping("/absences")
    public String pointageAbsences(@RequestParam(required = false) Long classeId,
                                   @RequestParam(required = false) Long coursId,
                                   Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);

        model.addAttribute("classes", teacherFacade.getClassesByTeacher(teacher.getId()));
        model.addAttribute("teacher", teacher);

        ClassDTO selectedClasse = null;
        CoursDTO selectedCours = null;

        if (classeId != null) {
             selectedClasse = teacherFacade.getClassById(classeId);
            model.addAttribute("selectedClasse", selectedClasse);

            List<CoursDTO> coursList = teacherFacade.getCoursByClasseAndTeacher(classeId, teacher.getId());
            model.addAttribute("coursList", coursList);

            if (coursId != null) {
                 selectedCours = teacherFacade.getCoursById(coursId);
                model.addAttribute("selectedCours", selectedCours);

                List<StudentDTO> students = teacherFacade.getStudentsByClass(classeId);
                model.addAttribute("students", students);
                model.addAttribute("totalStudents", students.size());

                List<AbsenceDTO> absences = teacherFacade.getAbsencesByCours(coursId);
                Map<Long, AbsenceDTO> absencesMap = absences.stream()
                        .collect(Collectors.toMap(a -> a.getEtudiant().getId(), a -> a));
                model.addAttribute("absencesMap", absencesMap);

                long absents = absencesMap.size();
                long presents = students.size() - absents;
                double tauxPresence = !students.isEmpty() ? (presents * 100.0) / students.size() : 0;

                model.addAttribute("presents", presents);
                model.addAttribute("absents", absents);
                model.addAttribute("tauxPresence", String.format("%.1f", tauxPresence));
            } else {
                List<StudentDTO> students = teacherFacade.getStudentsByClass(classeId);
                model.addAttribute("totalStudents", students.size());
            }
        }

        model.addAttribute("selectedClasse", selectedClasse);
        model.addAttribute("selectedCours", selectedCours);
        return "teacher/absence";
    }

    @PostMapping("/absences/save")
    public String saveAbsences(@RequestParam Long coursId,
                               @RequestParam Long classeId,
                               @RequestParam(required = false) List<Long> present,
                               @RequestParam(required = false) Map<Long, Boolean> justifiee,
                               @RequestParam(required = false) Map<Long, String> motif,
                               @RequestParam(required = false) Map<Long, TypeAbsence> typeAbsence) {

        teacherFacade.saveAbsences(coursId, present, justifiee, motif, typeAbsence);

        return "redirect:/teacher/absences?classeId=" + classeId + "&coursId=" + coursId;
    }


    //============MESSAGES=====

    @GetMapping("/messagerie")
    public String messagerie(Model model) {

        return "teacher/messagerie";
    }

    /*
    @GetMapping("/messagerie")
    public String messagerie(Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);

        List<MessageDTO> messages = teacherFacade.getMessagesByTeacher(teacher.getId());

        List<ParentDTO> parents = messages.stream().filter(
                m-> !Objects.equals(m.getSender().getId(), teacher.getUserAccount().getId()))
                .filter(m->)

        for (StudentDTO child : children) {
            teachers.addAll(parentFacade.getTeachersByStudent(child.getId()));
        }

        teachers = teachers.stream().distinct().collect(Collectors.toList());
        model.addAttribute("children", children);
        model.addAttribute("messages", messages);
        model.addAttribute("parent", parent);
        model.addAttribute("teachers", teachers);
        return "teacher/messagerie";
    }

    @GetMapping("/messagerie/conversation/{parentId}")
    public String conversation(@PathVariable Long teacherId, Model model) {
        ParentDTO parent = getCurrentParent();
        List<MessageDTO> conversation = parentFacade.getConversationWithTeacher(parent.getId(), teacherId);

        model.addAttribute("conversation", conversation);
        model.addAttribute("teacherId", teacherId);
        model.addAttribute("parent", parent);

        return "parent/messagerie";
    }
    @GetMapping("/api/conversation/{teacherId}")
    @ResponseBody
    public ResponseEntity<List<MessageDTO>> getConversation(@PathVariable Long teacherId) {
        try {
            ParentDTO parent = getCurrentParent();
            List<MessageDTO> conversation = parentFacade.getConversationWithTeacher(parent.getId(), teacherId);
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/messagerie/send")
    @ResponseBody
    public ResponseEntity<MessageDTO> sendMessage(@RequestParam Long teacherId,
                                                  @RequestParam String message) {
        ParentDTO parent = getCurrentParent();
        MessageDTO sentMessage = parentFacade.sendMessageToTeacher(parent.getId(), teacherId, message);

        return ResponseEntity.ok(sentMessage);
    }

     */


    //=====================================
    @GetMapping("/eval-form")
    public String evalForm(Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);

        model.addAttribute("evaluationDTO", new EvaluationDTO());
        model.addAttribute("classes", teacherFacade.getClassesByTeacher(teacher.getId()));
        model.addAttribute("teacher", teacher);
        return "teacher/evaluation-form";
    }

    @PostMapping("/addEval")
    public String addEval(@ModelAttribute EvaluationDTO evaluationDTO, Model model, BindingResult result) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);

        if (result.hasErrors()) {
            model.addAttribute("classes", teacherFacade.getClassesByTeacher(teacher.getId()));
            model.addAttribute("teacher", teacher);
            return "teacher/evaluation-form";
        }
        evaluationDTO.setEnseignant(teacher);

        teacherFacade.addEvaluation(evaluationDTO);
        return "redirect:/teacher/";
    }


    /**
     * Liste des évaluations de l’enseignant
     */
    @GetMapping("/evaluations")
    public String evaluations(Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);
        model.addAttribute("teacher", teacher);

        model.addAttribute("evaluations", teacherFacade.getEvaluationsByClass(teacher.getId()));
        model.addAttribute("activePage", "evaluations");

        return "teacher/evaluations";
    }

    @GetMapping("/classes")
    public String notesParClasse(@RequestParam(required = false) Long classeId, Model model) {
        String login = getCurrentUserLogin();
        TeacherDTO teacher = teacherFacade.findByLogin(login);

        model.addAttribute("teacher", teacher);
        model.addAttribute("classes", teacherFacade.getClassesByTeacher(teacher.getId()));

        if (classeId != null) {
            ClassDTO selectedClasse = teacherFacade.getClassById(classeId);
            model.addAttribute("selectedClasse", selectedClasse);

            List<StudentDTO> students = teacherFacade.getStudentsByClass(classeId);
            model.addAttribute("totalStudents", students.size());

            List<EvaluationDTO> evaluations = teacherFacade.getEvaluationsByClass(classeId);
            System.out.println("=== DEBUG: Nombre d'évaluations trouvées: " + evaluations.size());
            for (EvaluationDTO eval : evaluations) {
                System.out.println("  - Évaluation ID: " + eval.getId() + " - Titre: " + eval.getTitre());
            }
            model.addAttribute("evaluations", evaluations);
            model.addAttribute("totalEvaluations", evaluations.size());

            List<StudentNotesDTO> studentsNotes = new ArrayList<>();

            for (StudentDTO student : students) {
                StudentNotesDTO studentNotes = new StudentNotesDTO();
                studentNotes.setStudent(student);

                List<NoteDTO> notes = teacherFacade.getNotesByStudent(student.getId());
                System.out.println("=== DEBUG: Étudiant " + student.getPrenom() + " " + student.getNom() + " - Nombre de notes: " + (notes != null ? notes.size() : "null"));
                if (notes != null && !notes.isEmpty()) {
                    for (NoteDTO note : notes) {
                        System.out.println("  - Note: " + note.getValeur() + " pour évaluation ID: " + (note.getEvaluation() != null ? note.getEvaluation().getId() : "null"));
                    }
                }
                studentNotes.setNotes(notes);

                if (notes != null && !notes.isEmpty()) {
                    double moyenne = notes.stream()
                            .filter(n -> n.getValeur() != null)
                            .mapToDouble(NoteDTO::getValeur)
                            .average()
                            .orElse(0.0);
                    studentNotes.setMoyenne(moyenne);
                }

                studentsNotes.add(studentNotes);
            }

            model.addAttribute("studentsNotes", studentsNotes);

            double moyenneClasse = studentsNotes.stream()
                    .filter(sn -> sn.getMoyenne() != null)
                    .mapToDouble(StudentNotesDTO::getMoyenne)
                    .average()
                    .orElse(0.0);
            model.addAttribute("moyenneClasse", String.format("%.2f", moyenneClasse));

            long reussites = studentsNotes.stream()
                    .filter(sn -> sn.getMoyenne() != null && sn.getMoyenne() >= 10)
                    .count();
            double tauxReussite = !students.isEmpty() ? (reussites * 100.0) / students.size() : 0;
            model.addAttribute("tauxReussite", String.format("%.1f", tauxReussite));
        }

        return "teacher/classes";
    }









}
