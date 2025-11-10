package ci.ada.monetabv2new.controllers;

import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.repositories.MatiereRepository;
import ci.ada.monetabv2new.repositories.StudentRepository;
import ci.ada.monetabv2new.repositories.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SchoolController {

    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final MatiereRepository matiereRepo;


    @GetMapping("/students")
    public List<StudentEntity> getStudents(@RequestParam int year){
        return studentRepo.findAll();
    }

    @GetMapping("/teachers")
    public List<TeacherEntity> getTeachers(@RequestParam int year){
        return teacherRepo.findAll();
    }

    @GetMapping("/statistics")
    public Map<String, Long> getStatistics(){
        Map<String, Long> stats = new HashMap<>();
        stats.put("students", studentRepo.count());
        stats.put("teachers", teacherRepo.count());
        stats.put("matieres", matiereRepo.count());
        return stats;
    }
}
