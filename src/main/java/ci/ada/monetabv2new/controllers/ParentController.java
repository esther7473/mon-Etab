package ci.ada.monetabv2new.controllers;


import ci.ada.monetabv2new.services.dto.*;
import ci.ada.monetabv2new.services.facades.ParentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentFacade parentFacade;

    private ParentDTO getCurrentParent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        ParentDTO parent = parentFacade.getParent(login);
        if (parent == null) {
            throw new RuntimeException("Parent not found for authenticated user: " + login);
        }
        return parent;
    }

    @GetMapping("/")
    public String home(Model model) {
        ParentDTO parent = getCurrentParent();
        List<StudentDTO> children = parentFacade.getChildrenByParent(parent.getId());

        model.addAttribute("parent", parent);
        model.addAttribute("children", children);

        return "parent/home";
    }

    @GetMapping("/enfants")
    public String listChildren(Model model) {
        ParentDTO parent = getCurrentParent();
        List<StudentDTO> children = parentFacade.getChildrenByParent(parent.getId());

        model.addAttribute("children", children);
        return "parent/enfants";
    }

    @GetMapping("/enfant/{studentId}/notes")
    public String getStudentNotes(@PathVariable Long studentId, Model model) {
        ParentDTO parent = getCurrentParent();
        StudentDTO student = parentFacade.getStudentById(studentId, parent.getId());
        List<NoteDTO> notes = parentFacade.getNotesByStudent(studentId);
        Double moyenne = parentFacade.getMoyenneByStudent(studentId);

        model.addAttribute("student", student);
        model.addAttribute("notes", notes);
        model.addAttribute("moyenne", moyenne);

        return "parent/notes";
    }

    @GetMapping("/enfant/{studentId}/absences")
    public String getStudentAbsences(@PathVariable Long studentId, Model model) {
        ParentDTO parent = getCurrentParent();
        StudentDTO student = parentFacade.getStudentById(studentId, parent.getId());
        List<AbsenceDTO> absences = parentFacade.getAbsencesByStudent(studentId);

        model.addAttribute("student", student);
        model.addAttribute("absences", absences);

        return "parent/absences";
    }

    @GetMapping("/enfant/{studentId}/evaluations")
    public String getStudentEvaluations(@PathVariable Long studentId, Model model) {
        ParentDTO parent = getCurrentParent();
        StudentDTO student = parentFacade.getStudentById(studentId, parent.getId());
        List<EvaluationDTO> evaluations = parentFacade.getEvaluationsByStudent(studentId);

        model.addAttribute("student", student);
        model.addAttribute("evaluations", evaluations);

        return "parent/evaluations";
    }


    //==========MESSAGE=====
    @GetMapping("/messagerie")
    public String messagerie(Model model) {
        ParentDTO parent = getCurrentParent();
        List<MessageDTO> messages = parentFacade.getMessagesByParent(parent.getId());
        List<StudentDTO> children = parentFacade.getChildrenByParent(parent.getId());

        List<TeacherDTO> teachers = new ArrayList<>();

        for (StudentDTO child : children) {
            teachers.addAll(parentFacade.getTeachersByStudent(child.getId()));
        }

        teachers = teachers.stream().distinct().collect(Collectors.toList());
        model.addAttribute("children", children);
        model.addAttribute("messages", messages);
        model.addAttribute("parent", parent);
        model.addAttribute("teachers", teachers);
        return "parent/messagerie";
    }

    @GetMapping("/messagerie/conversation/{teacherId}")
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


    //=====RDV====================
    @GetMapping("/rdv")
    public String rdv(Model model) {
        ParentDTO parent = getCurrentParent();
        List<RdvDTO> rdvs = parentFacade.getRdvsByParent(parent.getId());
        List<StudentDTO> children = parentFacade.getChildrenByParent(parent.getId());

        model.addAttribute("rdvs", rdvs);
        model.addAttribute("children", children);

        return "parent/rdv";
    }

    @GetMapping("/rdv/nouveau")
    public String nouveauRdv(@RequestParam(required = false) Long studentId, Model model) {
        ParentDTO parent = getCurrentParent();
        List<StudentDTO> children = parentFacade.getChildrenByParent(parent.getId());

        model.addAttribute("children", children);

        if (studentId != null) {
            List<TeacherDTO> teachers = parentFacade.getTeachersByStudent(studentId);
            model.addAttribute("teachers", teachers);
            model.addAttribute("selectedStudentId", studentId);
        }

        return "parent/nouveau-rdv";
    }

    @PostMapping("/rdv/demander")
    public String demanderRdv(@RequestParam Long studentId,
                              @RequestParam Long teacherId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDebut,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureFin,
                              @RequestParam String motif,
                              RedirectAttributes redirectAttributes) {
        try {
            parentFacade.demanderRdv(studentId, teacherId, date, heureDebut, heureFin, motif);
            redirectAttributes.addFlashAttribute("success", "Demande de rendez-vous envoyée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la demande de rendez-vous: " + e.getMessage());
        }

        return "redirect:/parent/rdv";
    }

    @PostMapping("/rdv/{rdvId}/annuler")
    public String annulerRdv(@PathVariable Long rdvId, RedirectAttributes redirectAttributes) {
        try {
            parentFacade.annulerRdv(rdvId);
            redirectAttributes.addFlashAttribute("success", "Rendez-vous annulé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'annulation: " + e.getMessage());
        }

        return "redirect:/parent/rdv";
    }

    @GetMapping("/profil")
    public String profil(Model model) {
        ParentDTO parent = getCurrentParent();
        model.addAttribute("parent", parent);

        return "parent/profil";
    }

    @PostMapping("/profil/update")
    public String updateProfil(@ModelAttribute ParentDTO parentDTO, RedirectAttributes redirectAttributes) {
        try {
            ParentDTO currentParent = getCurrentParent();
            parentDTO.setId(currentParent.getId());
            parentFacade.partialUpdate(parentDTO);
            redirectAttributes.addFlashAttribute("success", "Profil mis à jour avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour: " + e.getMessage());
        }

        return "redirect:/parent/profil";
    }

    @GetMapping("/api/teachers/{studentId}")
    @ResponseBody
    public ResponseEntity<List<TeacherDTO>> getTeachersByStudent(@PathVariable Long studentId) {
        try {
            List<TeacherDTO> teachers = parentFacade.getTeachersByStudent(studentId);
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}