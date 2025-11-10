package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.MatiereEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.repositories.ClassRepository;
import ci.ada.monetabv2new.repositories.TeacherRepository;
import ci.ada.monetabv2new.services.TeacherService;
import ci.ada.monetabv2new.services.dto.TeacherDTO;
import ci.ada.monetabv2new.services.mapper.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherDTO save(TeacherDTO teacherDTO) {
        TeacherEntity entity = teacherMapper.toEntity(teacherDTO);
        entity = teacherRepository.save(entity);
        return teacherMapper.toDto(entity);
    }

    @Override
    public TeacherDTO partialUpdate(TeacherDTO teacherDTO) {
        if (Objects.isNull(teacherDTO.getId())) {
            throw new IllegalArgumentException("Teacher ID not exist");
        }

        return teacherRepository.findById(teacherDTO.getId())
                .map(entity -> {
                    if (teacherDTO.getIsVacant() != null) {
                        entity.setIsVacant(teacherDTO.getIsVacant());
                    }
                    if (teacherDTO.getMatiere() != null) {
                        entity.setMatiere(modelMapper.map(teacherDTO.getMatiere(),
                                MatiereEntity.class));
                    }
                    if (teacherDTO.getUserAccount() != null) {
                        entity.setUserAccountEntity(modelMapper.map(teacherDTO.getUserAccount(),
                                UserAccountEntity.class));
                    }
                    entity = teacherRepository.save(entity);
                    return teacherMapper.toDto(entity);
                })
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    }

    @Override
    public List<TeacherDTO> getAll() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toDto)
                .toList();
    }

    @Override
    public TeacherDTO getById(Long id) {
        return teacherRepository.findById(id)
                .map(teacherMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
    }

    @Override
    public void delete(Long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<TeacherDTO> teacherDTOS) {
        List<TeacherEntity> entities = teacherMapper.toEntityList(teacherDTOS);
        teacherRepository.deleteAll(entities);
    }

    @Override
    public Optional<TeacherDTO> findByLogin(UserAccountEntity login) {
        return teacherRepository.findByUserAccountEntity(login)
                .map(teacherMapper::toDto);
    }

    @Override
    public TeacherDTO addClassToTeacher(Long teacherId, Long classId) {
        TeacherEntity teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        ClassEntity classe = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        teacher.getClasses().add(classe);
        teacher = teacherRepository.save(teacher);

        return teacherMapper.toDto(teacher);
    }

    @Override
    public TeacherDTO removeClassFromTeacher(Long teacherId, Long classId) {
        TeacherEntity teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        ClassEntity classe = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        teacher.getClasses().remove(classe);
        teacher = teacherRepository.save(teacher);

        return teacherMapper.toDto(teacher);
    }

    @Override
    public List<TeacherDTO> getTeachersByClassId(Long classId) {
        ClassEntity classe = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));

        return classe.getTeachers().stream()
                .map(teacherMapper::toDto)
                .toList();
    }
}
