package ci.ada.monetabv2new.services.mapper;

import ci.ada.monetabv2new.models.RdvEntity;
import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.services.dto.RdvDTO;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import ci.ada.monetabv2new.services.dto.TeacherDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RdvMapperImpl implements RdvMapper {

    @Override
    public RdvDTO toDto(RdvEntity rdvEntity) {
        if (rdvEntity == null) return null;

        RdvDTO rdvDTO = new RdvDTO();
        rdvDTO.setId(rdvEntity.getId());
        rdvDTO.setDate(rdvEntity.getDate());
        rdvDTO.setMotif(rdvEntity.getMotif());
        rdvDTO.setHeureDebut(rdvEntity.getHeureDebut());
        rdvDTO.setHeureFin(rdvEntity.getHeureFin());
        rdvDTO.setStatut(rdvEntity.getStatut());

        if (rdvEntity.getStudent() != null) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(rdvEntity.getStudent().getId());
            rdvDTO.setStudent(studentDTO);
        }

        if (rdvEntity.getTeacher() != null) {
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setId(rdvEntity.getTeacher().getId());
            rdvDTO.setTeacher(teacherDTO);
        }

        return rdvDTO;
    }

    @Override
    public RdvEntity toEntity(RdvDTO rdvDTO) {
        if (rdvDTO == null) return null;

        RdvEntity rdvEntity = new RdvEntity();
        rdvEntity.setId(rdvDTO.getId());
        rdvEntity.setDate(rdvDTO.getDate());
        rdvEntity.setMotif(rdvDTO.getMotif());
        rdvEntity.setHeureDebut(rdvDTO.getHeureDebut());
        rdvEntity.setHeureFin(rdvDTO.getHeureFin());
        rdvEntity.setStatut(rdvDTO.getStatut());

        if (rdvDTO.getStudent() != null) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setId(rdvDTO.getStudent().getId());
            rdvEntity.setStudent(studentEntity);
        }

        if (rdvDTO.getTeacher() != null) {
            TeacherEntity teacherEntity = new TeacherEntity();
            teacherEntity.setId(rdvDTO.getTeacher().getId());
            rdvEntity.setTeacher(teacherEntity);
        }

        return rdvEntity;
    }

    @Override
    public List<RdvDTO> toDtoList(List<RdvEntity> rdvEntities) {
        if (rdvEntities == null) return List.of();
        return rdvEntities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RdvEntity> toEntityList(List<RdvDTO> rdvDTOS) {
        if (rdvDTOS == null) return List.of();
        return rdvDTOS.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
