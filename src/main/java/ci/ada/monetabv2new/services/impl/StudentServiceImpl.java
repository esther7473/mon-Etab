package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.repositories.StudentRepository;
import ci.ada.monetabv2new.services.ClassService;
import ci.ada.monetabv2new.services.StudentService;
import ci.ada.monetabv2new.services.UserService;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import ci.ada.monetabv2new.services.mapper.ClassMapper;
import ci.ada.monetabv2new.services.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ClassService classService;
    private final UserService userAccountService;
    private final ModelMapper modelMapper;
    private final ClassMapper classMapper;

    @Override
    public Optional<StudentDTO> findByUserAccount(UserAccountEntity userAccount) {
        return studentRepository.findByUserAccountEntity(userAccount)
                .map(studentMapper::toDto);
    }

    @Override
    public List<StudentDTO> getStudentsByClassId(Long classId) {
        return studentRepository.findByClassEntityId(classId).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDTO> findByLogin(UserAccountEntity login) {
        return studentRepository.findByUserAccountEntity(login)
                .map(studentMapper::toDto);

    }

    @Override
    public List<StudentDTO> getStudentsByParentId(Long parentId) {
        return studentRepository.findByParent_Id(parentId).stream().map(studentMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        StudentEntity entity = studentMapper.toEntity(studentDTO);
        entity = studentRepository.save(entity);
        return studentMapper.toDto(entity);
    }

    @Override
    public StudentDTO partialUpdate(StudentDTO studentDTO) {
        if (Objects.isNull(studentDTO.getId())) {
            throw new IllegalArgumentException("Student ID not exist");
        }

        return studentRepository.findById(studentDTO.getId())
                .map(entity -> {
                    if (studentDTO.getMatricule() != null) {
                        entity.setMatricule(studentDTO.getMatricule());
                    }
                    if (studentDTO.getNom() != null) {
                        entity.setNom(studentDTO.getNom());
                    }
                    if (studentDTO.getPrenom() != null) {
                        entity.setPrenom(studentDTO.getPrenom());
                    }
                    if (studentDTO.getTel() != null) {
                        entity.setTel(studentDTO.getTel());
                    }
                    if (studentDTO.getPays() != null) {
                        entity.setPays(studentDTO.getPays());
                    }
                    if (studentDTO.getPhoto() != null) {
                        entity.setPhoto(studentDTO.getPhoto());
                    }
                    if (studentDTO.getUserAccount() != null) {
                        entity.setUserAccountEntity(modelMapper.map(studentDTO.getUserAccount(), UserAccountEntity.class));
                    }
                    if (studentDTO.getClassDTO() != null) {
                        entity.setClassEntity(classMapper.toEntity(studentDTO.getClassDTO()));
                    }

                    entity = studentRepository.save(entity);
                    return studentMapper.toDto(entity);
                })
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
    }




    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<StudentDTO> d) {
        List<StudentEntity> entities = d.stream()
                .map(studentMapper::toEntity)
                .collect(Collectors.toList());
        studentRepository.deleteAll(entities);
    }
}
