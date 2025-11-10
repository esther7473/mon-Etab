package ci.ada.monetabv2new.models;


import ci.ada.monetabv2new.models.enumeration.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;


    @Enumerated(EnumType.STRING)
    private Role role;

}
