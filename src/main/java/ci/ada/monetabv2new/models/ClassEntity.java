package ci.ada.monetabv2new.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    private Integer maxStudent;

    @ManyToMany
    @JoinTable(
        name = "class_teacher",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<TeacherEntity> teachers;


    @OneToMany(mappedBy = "classEntity")
    private List<StudentEntity> students;

    @OneToMany(mappedBy = "classe")
    private List<CoursEntity> cours;

    @OneToMany(mappedBy = "classe")
    private List<EvaluationEntity> evaluations;



}
