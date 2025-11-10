package ci.ada.monetabv2new.services.mapper;

import ci.ada.monetabv2new.models.MatiereEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.MatiereDTO;
import ci.ada.monetabv2new.services.dto.TeacherDTO;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherMapperImpl implements TeacherMapper {

    private final ModelMapper modelMapper;

    @Override
    public TeacherDTO toDto(TeacherEntity teacherEntity) {
        if (teacherEntity == null) {
            return null;
        }

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacherEntity.getId());
        teacherDTO.setIsVacant(teacherEntity.getIsVacant());

        if (teacherEntity.getUserAccountEntity() != null) {
            teacherDTO.setUserAccount(modelMapper.map(teacherEntity.getUserAccountEntity(), UserAccountDTO.class));
        }

        if (teacherEntity.getMatiere() != null) {
            teacherDTO.setMatiere(modelMapper.map(teacherEntity.getMatiere(), MatiereDTO.class));
        }

        return teacherDTO;
    }

    @Override
    public TeacherEntity toEntity(TeacherDTO teacherDTO) {
        if (teacherDTO == null) {
            return null;
        }

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId(teacherDTO.getId());
        teacherEntity.setIsVacant(teacherDTO.getIsVacant());

        if (teacherDTO.getUserAccount() != null) {
            teacherEntity.setUserAccountEntity(modelMapper.map(teacherDTO.getUserAccount(),
                   UserAccountEntity.class));
        }

        if (teacherDTO.getMatiere() != null) {
            teacherEntity.setMatiere(modelMapper.map(teacherDTO.getMatiere(),
                    MatiereEntity.class));
        }

        return teacherEntity;
    }

    @Override
    public List<TeacherDTO> toDtoList(List<TeacherEntity> teacherEntities) {
        if (teacherEntities == null) {
            return List.of();
        }
        return teacherEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherEntity> toEntityList(List<TeacherDTO> teacherDTOS) {
        if (teacherDTOS == null) {
            return List.of();
        }
        return teacherDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
