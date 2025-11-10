package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.EvaluationEntity;
import ci.ada.monetabv2new.services.dto.EvaluationDTO;
import org.springframework.stereotype.Service;

@Service
public interface EvaluationMapper extends Mapper<EvaluationDTO, EvaluationEntity> {

}
