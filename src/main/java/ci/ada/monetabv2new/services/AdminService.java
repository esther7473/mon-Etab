package ci.ada.monetabv2new.services;


import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.AdminDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdminService extends CRUDService<AdminDTO> {

    AdminDTO getByLogin(UserAccountEntity login);
}
