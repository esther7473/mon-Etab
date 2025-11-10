package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.EvaluationEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.services.dto.ClassDTO;
import ci.ada.monetabv2new.services.dto.EvaluationDTO;
import ci.ada.monetabv2new.services.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationMapperImpl implements EvaluationMapper {

    private final ModelMapper modelMapper;

    @Override
    public EvaluationDTO toDto(EvaluationEntity evaluationEntity) {
        if (evaluationEntity == null) return null;

        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setId(evaluationEntity.getId());
        evaluationDTO.setTitre(evaluationEntity.getTitre());
        evaluationDTO.setDescription(evaluationEntity.getDescription());
        evaluationDTO.setDateCreation(evaluationEntity.getDateCreation());
        evaluationDTO.setDateRemise(evaluationEntity.getDateRemise());
        evaluationDTO.setTypeEvaluation(evaluationEntity.getTypeEvaluation());

        if (evaluationEntity.getClasse() != null) {
            ClassDTO classDTO = new ClassDTO();
            classDTO.setId(evaluationEntity.getClasse().getId());
            classDTO.setLabel(evaluationEntity.getClasse().getLabel());
            evaluationDTO.setClasse(classDTO);
        }

        if (evaluationEntity.getEnseignant() != null) {
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setId(evaluationEntity.getEnseignant().getId());
            evaluationDTO.setEnseignant(teacherDTO);
        }

        return evaluationDTO;
    }

    @Override
    public EvaluationEntity toEntity(EvaluationDTO evaluationDTO) {
        if (evaluationDTO == null) return null;

        EvaluationEntity evaluationEntity = new EvaluationEntity();
        evaluationEntity.setId(evaluationDTO.getId());
        evaluationEntity.setTitre(evaluationDTO.getTitre());
        evaluationEntity.setDescription(evaluationDTO.getDescription());
        evaluationEntity.setDateCreation(evaluationDTO.getDateCreation());
        evaluationEntity.setDateRemise(evaluationDTO.getDateRemise());
        evaluationEntity.setTypeEvaluation(evaluationDTO.getTypeEvaluation());

        if (evaluationDTO.getClasse() != null) {
            ClassEntity classEntity = new ClassEntity();
            classEntity.setId(evaluationDTO.getClasse().getId());
            classEntity.setLabel(evaluationDTO.getClasse().getLabel());
            evaluationEntity.setClasse(classEntity);
        }

        if (evaluationDTO.getEnseignant() != null) {
            TeacherEntity teacherEntity = new TeacherEntity();
            teacherEntity.setId(evaluationDTO.getEnseignant().getId());
            evaluationEntity.setEnseignant(teacherEntity);
        }

        return evaluationEntity;
    }

    @Override
    public List<EvaluationDTO> toDtoList(List<EvaluationEntity> evaluationEntities) {
        if (evaluationEntities == null) return List.of();
        return evaluationEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationEntity> toEntityList(List<EvaluationDTO> evaluationDTOS) {
        if (evaluationDTOS == null) return List.of();
        return evaluationDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
