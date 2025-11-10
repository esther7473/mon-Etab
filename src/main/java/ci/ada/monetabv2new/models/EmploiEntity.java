package ci.ada.monetabv2new.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@Setter
public class EmploiEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classe_id", nullable = false)
    private ClassEntity classe;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoursEntity> cours;

    @Column(nullable = false)
    private Integer semestre;

    @Column(nullable = false)
    private String anneeAcademique;
}
