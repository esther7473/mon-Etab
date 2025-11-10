package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.EvaluationEntity;
import ci.ada.monetabv2new.repositories.EvaluationRepository;
import ci.ada.monetabv2new.services.EvaluationService;
import ci.ada.monetabv2new.services.dto.EvaluationDTO;
import ci.ada.monetabv2new.services.mapper.EvaluationMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final ModelMapper modelMapper;
    private final EvaluationMapper evaluationMapper;


    @Override
    public EvaluationDTO save(EvaluationDTO evaluationDTO) {
        EvaluationEntity entity = evaluationMapper.toEntity(evaluationDTO);
        entity.setDateCreation(LocalDateTime.now());
        EvaluationEntity saved = evaluationRepository.save(entity);
        return evaluationMapper.toDto(saved);
    }

    @Override
    public EvaluationDTO partialUpdate(EvaluationDTO evaluationDTO) {
        return evaluationRepository.findById(evaluationDTO.getId())
                .map(existing -> {
                    modelMapper.map(evaluationDTO, existing); // mise à jour partielle
                    EvaluationEntity updated = evaluationRepository.save(existing);
                    return modelMapper.map(updated, EvaluationDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("Évaluation introuvable avec id " + evaluationDTO.getId()));
    }

    @Override
    public List<EvaluationDTO> getAll() {
        return evaluationRepository.findAll().stream()
                .map(evaluationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationDTO getById(Long id) {
        return evaluationRepository.findById(id)
                .map(evaluationMapper::toDto).orElseThrow(() -> new IllegalArgumentException("Evaluation not found"));
    }

    @Override
    public void delete(Long id) {
        evaluationRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<EvaluationDTO> d) {

    }

    @Override
    public List<EvaluationDTO> getByClasse(Long classeId) {
        return evaluationRepository.findByClasseId(classeId).stream()
                .map(evaluationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationDTO> getByTeacher(Long teacherId) {
        return evaluationRepository.findByEnseignantId(teacherId).stream()
                .map(evaluationMapper::toDto)
                .collect(Collectors.toList());


    }

    @Override
    public List<EvaluationDTO> getByStudent(Long studentId) {
        return null;
                /*evaluationRepository.findByStudentId(studentId).stream()
                .map(e -> modelMapper.map(e, EvaluationDTO.class))
                .collect(Collectors.toList());

                 */
    }
}

