package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.CoursEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.repositories.CoursRepository;
import ci.ada.monetabv2new.services.CoursService;
import ci.ada.monetabv2new.services.dto.CoursDTO;
import ci.ada.monetabv2new.services.mapper.CoursMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursServiceImpl implements CoursService {

    private final CoursRepository coursRepository;
    private final ModelMapper modelMapper;
    private final CoursMapper coursMapper;

    @Override
    public List<CoursDTO> findByTeacherId(Long teacherId) {
        TeacherEntity enseignant = new TeacherEntity();
        enseignant.setId(teacherId);

        return coursRepository.findByEnseignant(enseignant)
                .stream()
                .map(coursMapper::toDto)
                .toList();
    }

    @Override
    public List<CoursDTO> findByClassId(Long classId) {
        ClassEntity classe = new ClassEntity();
        classe.setId(classId);

        return coursRepository.findByClasse(classe)
                .stream()
                .map(coursMapper::toDto)
                .toList();
    }

    @Override
    public List<CoursDTO> findByDate(LocalDate date) {
        return coursRepository.findByDateCours(date)
                .stream()
                .map(coursMapper::toDto)
                .toList();
    }

    @Override
    public List<CoursDTO> findConflictingSalle(String salle, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        return coursRepository.findConflictingSalle(salle, date, heureDebut, heureFin)
                .stream()
                .map(coursMapper::toDto)
                .toList();
    }

    @Override
    public List<CoursDTO> findConflictingTeacher(Long enseignantId, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        TeacherEntity enseignant = new TeacherEntity();
        enseignant.setId(enseignantId);

        return coursRepository.findConflictingTeacher(enseignant, date, heureDebut, heureFin)
                .stream()
                .map(coursMapper::toDto)
                .toList();
    }

    @Override
    public List<CoursDTO> findByTeacherAndDateRange(Long teacherId, LocalDate startDate, LocalDate endDate) {
        TeacherEntity enseignant = new TeacherEntity();
        enseignant.setId(teacherId);

        return coursRepository.findByEnseignantAndDateCoursBetweenOrderByDateCoursAscHeureDebutAsc(
                        enseignant, startDate, endDate
                )
                .stream()
                .map(coursMapper::toDto)
                .toList();
    }

    @Override
    public List<CoursDTO> findByClassAndTeacher(Long teacherId, Long classId) {
        ClassEntity classe = new ClassEntity();
        classe.setId(classId);
        TeacherEntity enseignant = new TeacherEntity();
        enseignant.setId(teacherId);
        return coursRepository.findByEnseignantAndClasse(enseignant,classe).stream().map(coursMapper::toDto).toList();
    }

    @Override
    public CoursDTO save(CoursDTO coursDTO) {
        CoursEntity entity = coursMapper.toEntity(coursDTO);
        return coursMapper.toDto(coursRepository.save(entity));
    }

    @Override
    public CoursDTO partialUpdate(CoursDTO coursDTO) {
        return save(coursDTO);
    }

    @Override
    public List<CoursDTO> getAll() {
        return coursRepository.findAll()
                .stream()
                .map(coursMapper::toDto)
                .toList();
    }

    @Override
    public CoursDTO getById(Long id) {
        return coursRepository.findById(id)
                .map(coursMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        coursRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<CoursDTO> d) {
        coursRepository.deleteAll(
                d.stream().map(coursMapper::toEntity).toList()
        );
    }
}
