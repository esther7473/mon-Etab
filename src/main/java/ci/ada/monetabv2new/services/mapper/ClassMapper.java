package ci.ada.monetabv2new.services.mapper;



import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.services.dto.ClassDTO;
import org.springframework.stereotype.Service;

@Service
public interface ClassMapper extends Mapper<ClassDTO, ClassEntity> {
}
