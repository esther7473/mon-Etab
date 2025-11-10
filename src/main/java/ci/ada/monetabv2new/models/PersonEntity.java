package ci.ada.monetabv2new.models;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@MappedSuperclass
@Getter
@Setter
public abstract class PersonEntity {

    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private LocalDate birthDate;
    private String pays;
    private String photo;
    private String slug;
}