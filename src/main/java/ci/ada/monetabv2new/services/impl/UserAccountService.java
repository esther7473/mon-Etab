package ci.ada.monetabv2new.services.impl;



import ci.ada.monetabv2new.services.CRUDService;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;

import java.util.Optional;

public interface UserAccountService extends CRUDService<UserAccountDTO>
{
    UserAccountDTO saveWithEncodedPassword(UserAccountDTO user);
    UserAccountDTO findByLoginAndPassword(UserAccountDTO user);
   Optional < UserAccountDTO> findByLogin(String login);
}
