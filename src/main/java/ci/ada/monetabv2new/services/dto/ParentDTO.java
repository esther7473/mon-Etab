package ci.ada.monetabv2new.services.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ParentDTO  extends  PersonDTO{

    private Long id;

    private UserAccountDTO userAccount;
}
