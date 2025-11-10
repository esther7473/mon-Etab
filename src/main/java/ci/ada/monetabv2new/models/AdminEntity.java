package ci.ada.monetabv2new.models;

import ci.ada.monetabv2new.models.enumeration.Poste;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AdminEntity  extends PersonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Poste poste;


    @OneToOne
    private UserAccountEntity userAccountEntity;
}
