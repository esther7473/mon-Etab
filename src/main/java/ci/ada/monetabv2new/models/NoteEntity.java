package ci.ada.monetabv2new.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notes")
@Getter
@Setter
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double valeur;
    private Double coefficient;
    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private EvaluationEntity evaluation;
    private String appreciation;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private StudentEntity etudiant;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private TeacherEntity enseignant;
}