package ci.ada.monetabv2new.models;

import ci.ada.monetabv2new.models.enumeration.TypeCours;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
public class CoursEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity enseignant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classe_id")
    private ClassEntity classe;

    @Column(nullable = false)
    private String salle;

    @Column(nullable = false)
    private LocalDate dateCours;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCours typeCours;


    @OneToMany(mappedBy = "cours")
    private List<AbsenceEntity> absences;



}
