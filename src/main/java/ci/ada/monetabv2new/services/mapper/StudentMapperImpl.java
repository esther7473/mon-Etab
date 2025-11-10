package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.ParentEntity;
import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.ClassDTO;
import ci.ada.monetabv2new.services.dto.ParentDTO;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentMapperImpl implements StudentMapper {
    private final ModelMapper modelMapper;

    @Override
    public StudentDTO toDto(StudentEntity studentEntity) {
        if (studentEntity == null) {
            return null;
        }

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(studentEntity.getId());
        studentDTO.setMatricule(studentEntity.getMatricule());
        studentDTO.setNom(studentEntity.getNom());
        studentDTO.setPrenom(studentEntity.getPrenom());
        studentDTO.setBirthDate(studentEntity.getBirthDate());
        studentDTO.setPays(studentEntity.getPays());
        studentDTO.setPhoto(studentEntity.getPhoto());
        studentDTO.setTel(studentEntity.getTel());


        if (studentEntity.getUserAccountEntity() != null) {
            studentDTO.setUserAccount(
                    modelMapper.map(studentEntity.getUserAccountEntity(), UserAccountDTO.class)
            );
        }

        if (studentEntity.getClassEntity() != null) {
            ClassDTO classDTO = new ClassDTO();
            classDTO.setId(studentEntity.getClassEntity().getId());
            classDTO.setLabel(studentEntity.getClassEntity().getLabel());
            classDTO.setMaxStudent(studentEntity.getClassEntity().getMaxStudent());
            studentDTO.setClassDTO(classDTO);
        }

        if (studentEntity.getParent() != null) {
            ParentDTO parentDTO = new ParentDTO();
            parentDTO.setId(studentEntity.getParent().getId());
            parentDTO.setUserAccount(modelMapper.map(studentEntity.getParent().getUserAccount(), UserAccountDTO.class));
            studentDTO.setParent(parentDTO);
        }
        return studentDTO;
    }

    @Override
    public StudentEntity toEntity(StudentDTO studentDTO) {
        if (studentDTO == null) {
            return null;
        }

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentDTO.getId());
        studentEntity.setMatricule(studentDTO.getMatricule());
        studentEntity.setNom(studentDTO.getNom());
        studentEntity.setPrenom(studentDTO.getPrenom());
        studentEntity.setBirthDate(studentDTO.getBirthDate());
        studentEntity.setPays(studentDTO.getPays());
        studentEntity.setPhoto(studentDTO.getPhoto());
        studentEntity.setTel(studentDTO.getTel());


        if (studentDTO.getUserAccount() != null) {
            studentEntity.setUserAccountEntity(
                    modelMapper.map(studentDTO.getUserAccount(), UserAccountEntity.class)
            );
        }

        if (studentDTO.getClassDTO() != null) {
            ClassEntity classEntity = new ClassEntity();
            classEntity.setId(studentDTO.getClassDTO().getId());
            classEntity.setLabel(studentDTO.getClassDTO().getLabel());
            classEntity.setMaxStudent(studentDTO.getClassDTO().getMaxStudent());
            studentEntity.setClassEntity(classEntity);
        }
        if (studentEntity.getParent() != null) {
            ParentEntity parentDTO = new ParentEntity();
            parentDTO.setId(studentDTO.getParent().getId());
            parentDTO.setUserAccount(modelMapper.map(studentDTO.getParent().getUserAccount(), UserAccountEntity.class));
            studentEntity.setParent(parentDTO);
        }

        return studentEntity;
    }

    @Override
    public List<StudentDTO> toDtoList(List<StudentEntity> studentEntities) {
        if (studentEntities == null) {
            return List.of();
        }
        return studentEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentEntity> toEntityList(List<StudentDTO> studentDTOS) {
        if (studentDTOS == null) {
            return List.of();
        }
        return studentDTOS.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
