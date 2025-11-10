package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.AbsenceEntity;
import ci.ada.monetabv2new.models.CoursEntity;
import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.AbsenceDTO;
import ci.ada.monetabv2new.services.dto.CoursDTO;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AbsenceMapperImpl implements AbsenceMapper {
    private final ModelMapper modelMapper;


    @Override
    public AbsenceDTO toDto(AbsenceEntity absenceEntity) {
        if (absenceEntity == null) {
            return null;
        }

        AbsenceDTO absenceDTO = new AbsenceDTO();
        absenceDTO.setId(absenceEntity.getId());

        if (absenceEntity.getEtudiant() != null) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(absenceEntity.getEtudiant().getId());

            if (absenceEntity.getEtudiant().getUserAccountEntity() != null) {
                studentDTO.setUserAccount(
                        modelMapper.map(absenceEntity.getEtudiant().getUserAccountEntity(), UserAccountDTO.class)
                );
            }

            absenceDTO.setEtudiant(studentDTO);
        }

        absenceDTO.setTypeAbsence(absenceEntity.getTypeAbsence());
        absenceDTO.setJustifiee(absenceEntity.getJustifiee());
        absenceDTO.setMotif(absenceEntity.getMotif());

        CoursDTO coursDTO = new CoursDTO();
        coursDTO.setId(absenceEntity.getCours().getId());
        coursDTO.setDateCours(absenceEntity.getCours().getDateCours());

        absenceDTO.setCours(coursDTO);

        return absenceDTO;
    }

    @Override
    public AbsenceEntity toEntity(AbsenceDTO absenceDTO) {
        if (absenceDTO == null) {
            return null;
        }

        AbsenceEntity absenceEntity = new AbsenceEntity();
        absenceEntity.setId(absenceDTO.getId());

        if (absenceDTO.getEtudiant() != null) {
            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setId(absenceDTO.getEtudiant().getId());

            if (absenceDTO.getEtudiant().getUserAccount() != null) {
                // On délègue à ModelMapper
                studentEntity.setUserAccountEntity(
                        modelMapper.map(absenceDTO.getEtudiant().getUserAccount(), UserAccountEntity.class)
                );
            }

            absenceEntity.setEtudiant(studentEntity);
        }

        absenceEntity.setTypeAbsence(absenceDTO.getTypeAbsence());
        absenceEntity.setJustifiee(absenceDTO.getJustifiee());
        absenceEntity.setMotif(absenceDTO.getMotif());

        absenceEntity.setCours(modelMapper.map(absenceDTO.getCours(), CoursEntity.class));
        return absenceEntity;
    }

    @Override
    public List<AbsenceDTO> toDtoList(List<AbsenceEntity> absenceEntities) {
        if (absenceEntities == null) {
            return List.of();
        }
        return absenceEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AbsenceEntity> toEntityList(List<AbsenceDTO> absenceDTOS) {
        if (absenceDTOS == null) {
            return List.of();
        }
        return absenceDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
