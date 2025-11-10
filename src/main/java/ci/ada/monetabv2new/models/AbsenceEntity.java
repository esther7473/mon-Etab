package ci.ada.monetabv2new.models;

import ci.ada.monetabv2new.models.enumeration.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AbsenceEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeAbsence typeAbsence; // ABSENCE, RETARD


    private Boolean justifiee;
    private String motif;

    @ManyToOne
    private StudentEntity etudiant;

    @ManyToOne
    private CoursEntity cours;


}
