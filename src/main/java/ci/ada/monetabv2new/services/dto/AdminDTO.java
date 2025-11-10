package ci.ada.monetabv2new.services.dto;


import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.models.enumeration.Poste;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDTO extends PersonDTO{

    private Long id;
    private Poste poste;
    private UserAccountEntity userAccountEntity;

}
