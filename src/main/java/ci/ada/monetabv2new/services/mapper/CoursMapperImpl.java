package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.CoursEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoursMapperImpl implements CoursMapper {

    private final ModelMapper modelMapper;

    @Override
    public CoursDTO toDto(CoursEntity coursEntity) {
        if (coursEntity == null) return null;

        CoursDTO coursDTO = new CoursDTO();
        coursDTO.setId(coursEntity.getId());
        coursDTO.setDateCours(coursEntity.getDateCours());
        coursDTO.setHeureDebut(coursEntity.getHeureDebut());
        coursDTO.setHeureFin(coursEntity.getHeureFin());
        coursDTO.setSalle(coursEntity.getSalle());
        coursDTO.setTypeCours(coursEntity.getTypeCours());

        if (coursEntity.getClasse() != null) {
            ClassDTO classDTO = new ClassDTO();
            classDTO.setId(coursEntity.getClasse().getId());
            classDTO.setLabel(coursEntity.getClasse().getLabel());
            coursDTO.setClasse(classDTO);
        }

        if (coursEntity.getEnseignant() != null) {
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setId(coursEntity.getEnseignant().getId());
            teacherDTO.setMatiere(modelMapper.map(coursEntity.getEnseignant().getMatiere(), MatiereDTO.class));
            if (coursEntity.getEnseignant().getUserAccountEntity() != null) {
                teacherDTO.setUserAccount(modelMapper.map(
                        coursEntity.getEnseignant().getUserAccountEntity(),
                        UserAccountDTO.class
                ));
            }
            coursDTO.setEnseignant(teacherDTO);
        }

        return coursDTO;
    }

    @Override
    public CoursEntity toEntity(CoursDTO coursDTO) {
        if (coursDTO == null) return null;

        CoursEntity coursEntity = new CoursEntity();
        coursEntity.setId(coursDTO.getId());
        coursEntity.setDateCours(coursDTO.getDateCours());
        coursEntity.setHeureDebut(coursDTO.getHeureDebut());
        coursEntity.setHeureFin(coursDTO.getHeureFin());
        coursEntity.setSalle(coursDTO.getSalle());
        coursEntity.setTypeCours(coursDTO.getTypeCours());

        if (coursDTO.getClasse() != null) {
            ClassEntity classEntity = new ClassEntity();
            classEntity.setId(coursDTO.getClasse().getId());
            classEntity.setLabel(coursDTO.getClasse().getLabel());
            coursEntity.setClasse(classEntity);
        }

        if (coursDTO.getEnseignant() != null) {
            TeacherEntity teacherEntity = new TeacherEntity();
            teacherEntity.setId(coursDTO.getEnseignant().getId());
            if (coursDTO.getEnseignant().getUserAccount() != null) {
                teacherEntity.setUserAccountEntity(modelMapper.map(
                        coursDTO.getEnseignant().getUserAccount(),
                        UserAccountEntity.class
                ));
            }
            coursEntity.setEnseignant(teacherEntity);
        }

        return coursEntity;
    }

    @Override
    public List<CoursDTO> toDtoList(List<CoursEntity> coursEntities) {
        if (coursEntities == null) return List.of();
        return coursEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CoursEntity> toEntityList(List<CoursDTO> coursDTOS) {
        if (coursDTOS == null) return List.of();
        return coursDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
