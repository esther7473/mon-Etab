package ci.ada.monetabv2new.services.impl;

import ci.ada.monetabv2new.repositories.AbsenceRepository;
import ci.ada.monetabv2new.services.AbsenceService;
import ci.ada.monetabv2new.services.dto.AbsenceDTO;
import ci.ada.monetabv2new.services.dto.CoursDTO;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import ci.ada.monetabv2new.services.mapper.AbsenceMapper;
import ci.ada.monetabv2new.services.mapper.CoursMapper;
import ci.ada.monetabv2new.services.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceServiceImpl implements AbsenceService {
    private  final AbsenceMapper absenceMapper;
    private final AbsenceRepository absenceRepository;
    private final StudentMapper studentMapper;
    private final CoursMapper coursMapper;

    @Override
    public List<AbsenceDTO> getAllByStudentId(StudentDTO student) {
        return absenceRepository.findByEtudiant(studentMapper.toEntity(student)).stream().map(absenceMapper::toDto).toList();
    }

    @Override
    public List<AbsenceDTO> getAllByCoursId(CoursDTO cours) {
        return absenceRepository.findByCours(coursMapper.toEntity(cours)).stream().map(absenceMapper::toDto).toList();
    }

    @Override
    public void deleteByCoursAndStudent(CoursDTO coursDTO, StudentDTO studentDTO) {
        absenceRepository.deleteByCoursAndEtudiant(coursMapper.toEntity(coursDTO), studentMapper.toEntity(studentDTO));
    }

    @Override
    public AbsenceDTO save(AbsenceDTO absenceDTO) {
        return absenceMapper.toDto(absenceRepository.save(absenceMapper.toEntity(absenceDTO)));
    }

    @Override
    public AbsenceDTO partialUpdate(AbsenceDTO absenceDTO) {
        return null;
    }

    @Override
    public List<AbsenceDTO> getAll() {
        return absenceRepository.findAll().stream().map(absenceMapper::toDto).toList();
    }

    @Override
    public AbsenceDTO getById(Long id) {
        return absenceMapper.toDto(absenceRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Long id) {
        absenceRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<AbsenceDTO> d) {
    }
}
