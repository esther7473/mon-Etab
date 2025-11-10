package ci.ada.monetabv2new.services;



import ci.ada.monetabv2new.services.dto.EvaluationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EvaluationService extends CRUDService<EvaluationDTO> {

    List<EvaluationDTO> getByClasse(Long classeId) ;
    List<EvaluationDTO> getByTeacher(Long teacherId);

    List<EvaluationDTO> getByStudent(Long studentId);
}
