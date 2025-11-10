package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.services.dto.ClassDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassMapperImpl implements ClassMapper {
    @Override
    public ClassDTO toDto(ClassEntity classEntity) {
        if (classEntity == null) {
            return null;
        }

        ClassDTO classDTO = new ClassDTO();
        classDTO.setId(classEntity.getId());
        classDTO.setLabel(classEntity.getLabel());
        classDTO.setMaxStudent(classEntity.getMaxStudent());

        // ⚠️ tu avais déjà commencé à mapper teachers et students dans le DTO ailleurs
        // Ici je laisse vide, sauf si tu veux inclure aussi les IDs des relations.
        // Sinon, on ne change rien.

        return classDTO;
    }

    @Override
    public ClassEntity toEntity(ClassDTO classDTO) {
        if (classDTO == null) {
            return null;
        }

        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(classDTO.getId());
        classEntity.setLabel(classDTO.getLabel());
        classEntity.setMaxStudent(classDTO.getMaxStudent());

        // idem : pas de mapping direct pour teachers/students pour éviter boucle

        return classEntity;
    }

    @Override
    public List<ClassDTO> toDtoList(List<ClassEntity> classEntities) {
        if (classEntities == null) {
            return List.of();
        }
        return classEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassEntity> toEntityList(List<ClassDTO> classDTOS) {
        if (classDTOS == null) {
            return List.of();
        }
        return classDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
