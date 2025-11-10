package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.*;
import ci.ada.monetabv2new.services.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteMapperImpl implements NoteMapper {

    private final ModelMapper modelMapper;

    @Override
    public NoteDTO toDto(NoteEntity noteEntity) {
        if (noteEntity == null) {
            return null;
        }

        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(noteEntity.getId());
        noteDTO.setValeur(noteEntity.getValeur());
        noteDTO.setCoefficient(noteEntity.getCoefficient());
        noteDTO.setAppreciation(noteEntity.getAppreciation());

        if (noteEntity.getEvaluation() != null) {
            EvaluationDTO evaluationDTO = new EvaluationDTO();
            evaluationDTO.setId(noteEntity.getEvaluation().getId());
            noteDTO.setEvaluation(evaluationDTO);
        }

        if (noteEntity.getEtudiant() != null) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(noteEntity.getEtudiant().getId());
            noteDTO.setEtudiant(studentDTO);
        }

        if (noteEntity.getEnseignant() != null) {
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setId(noteEntity.getEnseignant().getId());
            teacherDTO.setMatiere(modelMapper.map(noteEntity.getEnseignant().getMatiere(), MatiereDTO.class));
            noteDTO.setEnseignant(teacherDTO);
        }

        return noteDTO;
    }

    @Override
    public NoteEntity toEntity(NoteDTO noteDTO) {
        if (noteDTO == null) {
            return null;
        }

        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setId(noteDTO.getId());
        noteEntity.setValeur(noteDTO.getValeur());
        noteEntity.setCoefficient(noteDTO.getCoefficient());
        noteEntity.setAppreciation(noteDTO.getAppreciation());

        if (noteDTO.getEvaluation() != null) {
            EvaluationEntity evaluation = new EvaluationEntity();
            evaluation.setId(noteDTO.getEvaluation().getId());
            noteEntity.setEvaluation(evaluation);
        }

        if (noteDTO.getEtudiant() != null) {
            StudentEntity student = new StudentEntity();
            student.setId(noteDTO.getEtudiant().getId());
            noteEntity.setEtudiant(student);
        }

        if (noteDTO.getEnseignant() != null) {
            TeacherEntity teacher = new TeacherEntity();
            teacher.setId(noteDTO.getEnseignant().getId());
            teacher.setMatiere(modelMapper.map(noteDTO.getEnseignant().getMatiere(), MatiereEntity.class));
            noteEntity.setEnseignant(teacher);
        }

        return noteEntity;
    }

    @Override
    public List<NoteDTO> toDtoList(List<NoteEntity> noteEntities) {
        if (noteEntities == null) {
            return List.of();
        }
        return noteEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteEntity> toEntityList(List<NoteDTO> noteDTOS) {
        List<NoteEntity> noteEntities = new ArrayList<>();

        noteEntities = noteDTOS.stream().map(this::toEntity).toList();
        return noteEntities;
    }

}
