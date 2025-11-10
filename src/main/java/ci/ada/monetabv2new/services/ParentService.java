package ci.ada.monetabv2new.services;


import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.ParentDTO;
import org.springframework.stereotype.Service;

@Service
public interface ParentService extends CRUDService<ParentDTO> {
    ParentDTO getByLogin(UserAccountEntity login);

}
