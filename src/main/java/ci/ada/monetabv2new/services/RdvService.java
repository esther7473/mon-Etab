package ci.ada.monetabv2new.services;

import ci.ada.monetabv2new.services.dto.RdvDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RdvService extends CRUDService<RdvDTO> {
    List<RdvDTO> getRdvsByStudent(Long id);
    List<RdvDTO> getRdvsByTeacher(Long id);
    List<RdvDTO> getRdvsByParent(Long id);
}
