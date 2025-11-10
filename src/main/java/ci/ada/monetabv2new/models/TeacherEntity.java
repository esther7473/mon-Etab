package ci.ada.monetabv2new.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TeacherEntity extends PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isVacant;

    @ManyToOne
    @JoinColumn(name = "matiere_id")
    private MatiereEntity matiere;

    @OneToOne
    private UserAccountEntity userAccountEntity;

    // Relations
    @ManyToMany(mappedBy = "teachers")
    private List<ClassEntity> classes;

    @OneToMany(mappedBy = "enseignant")
    private List<CoursEntity> cours;

    @OneToMany(mappedBy = "enseignant")
    private List<EvaluationEntity> evaluations;
}