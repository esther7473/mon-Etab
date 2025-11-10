package ci.ada.monetabv2new.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class StudentEntity extends PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matricule;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClassEntity classEntity;

    @OneToOne
    private UserAccountEntity userAccountEntity;

    @OneToMany(mappedBy = "etudiant")
    private List<AbsenceEntity> absences;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ParentEntity parent;
}