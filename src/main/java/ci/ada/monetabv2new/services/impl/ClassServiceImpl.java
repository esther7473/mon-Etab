package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.repositories.ClassRepository;
import ci.ada.monetabv2new.repositories.TeacherRepository;
import ci.ada.monetabv2new.services.ClassService;
import ci.ada.monetabv2new.services.dto.ClassDTO;
import ci.ada.monetabv2new.services.mapper.ClassMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;
    private final ClassMapper classMapper;

    @Override
    public ClassDTO save(ClassDTO classDTO) {
        ClassEntity entity = classMapper.toEntity(classDTO);
        entity = classRepository.save(entity);
        return classMapper.toDto(entity);
    }

    @Override
    public ClassDTO partialUpdate(ClassDTO classDTO) {
        if (Objects.isNull(classDTO.getId())) {
            throw new IllegalArgumentException("Class ID not exist");
        }

        return classRepository.findById(classDTO.getId())
                .map(entity -> {
                    if (Objects.nonNull(classDTO.getLabel())) {
                        entity.setLabel(classDTO.getLabel());
                    }
                    if (Objects.nonNull(classDTO.getMaxStudent())) {
                        entity.setMaxStudent(classDTO.getMaxStudent());
                    }
                    entity = classRepository.save(entity);
                    return classMapper.toDto(entity);
                })
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
    }

    @Override
    public List<ClassDTO> getAll() {
        return classRepository.findAll().stream()
                .map(classMapper::toDto)
                .toList();
    }

    @Override
    public ClassDTO getById(Long id) {
        return classRepository.findById(id)
                .map(classMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
    }

    @Override
    public void delete(Long id) {
        classRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<ClassDTO> classes) {
        List<ClassEntity> entities = classMapper.toEntityList(classes);
        classRepository.deleteAll(entities);
    }

    // ====================== Relations avec Teacher ======================

    @Override
    public ClassDTO addTeacherToClass(Long classId, Long teacherId) {
        ClassEntity classe = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        TeacherEntity teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if (!classe.getTeachers().contains(teacher)) {
            classe.getTeachers().add(teacher);
        }

        classe = classRepository.save(classe);
        return classMapper.toDto(classe);
    }

    @Override
    public ClassDTO removeTeacherFromClass(Long classId, Long teacherId) {
        ClassEntity classe = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class not found"));
        TeacherEntity teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        classe.getTeachers().remove(teacher);
        classe = classRepository.save(classe);

        return classMapper.toDto(classe);
    }

    @Override
    public List<ClassDTO> getClassesWithTeachers() {
        return classRepository.findAll().stream()
                .filter(c -> c.getTeachers() != null && !c.getTeachers().isEmpty())
                .map(classMapper::toDto)
                .toList();
    }

    @Override
    public List<ClassDTO> getClassesByTeacherId(Long teacherId) {
        TeacherEntity teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        return teacher.getClasses().stream()
                .map(classMapper::toDto)
                .toList();
    }
}
