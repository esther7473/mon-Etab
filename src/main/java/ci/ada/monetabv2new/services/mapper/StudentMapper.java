package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import org.springframework.stereotype.Service;

@Service
public interface StudentMapper extends Mapper<StudentDTO, StudentEntity> {
}
