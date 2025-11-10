package ci.ada.monetabv2new.services.impl;



import java.util.ArrayList;
import java.util.List;

import ci.ada.monetabv2new.models.NoteEntity;
import ci.ada.monetabv2new.repositories.NoteRepository;
import ci.ada.monetabv2new.services.NoteService;
import ci.ada.monetabv2new.services.dto.NoteDTO;
import ci.ada.monetabv2new.services.mapper.NoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;




@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;


    @Override
    public List<NoteDTO> getNotesByStudent(Long studentId) {
        List<NoteEntity> notes = noteRepository.findByEtudiant_Id(studentId);
        return noteMapper.toDtoList(notes);
    }

    @Override
    public List<NoteDTO> getNotesByEvaluation(Long evaluationId) {
        List<NoteEntity> notes = noteRepository.findByEvaluation_Id(evaluationId);
        return noteMapper.toDtoList(notes);
    }

    @Override
    public void saveAll(List<NoteDTO> notes) {
        List<NoteEntity> entities = new ArrayList<>();
        for (NoteDTO note : notes) {
            entities.add(noteMapper.toEntity(note));
        }
        noteRepository.saveAll(entities);
    }

    @Override
    public NoteDTO save(NoteDTO noteDTO) {
        NoteEntity entity = noteMapper.toEntity(noteDTO);
        NoteEntity saved = noteRepository.save(entity);
        return noteMapper.toDto(saved);
    }

    @Override
    public NoteDTO partialUpdate(NoteDTO noteDTO) {
        Optional<NoteEntity> existingOpt = noteRepository.findById(noteDTO.getId());
        if (existingOpt.isPresent()) {
            NoteEntity existing = existingOpt.get();

            if (noteDTO.getValeur() != null) {
                existing.setValeur(noteDTO.getValeur());
            }
            if (noteDTO.getCoefficient() != null) {
                existing.setCoefficient(noteDTO.getCoefficient());
            }
            if (noteDTO.getAppreciation() != null) {
                existing.setAppreciation(noteDTO.getAppreciation());
            }
            if (noteDTO.getEvaluation() != null) {
                existing.getEvaluation().setId(noteDTO.getEvaluation().getId());
            }
            if (noteDTO.getEtudiant() != null) {
                existing.getEtudiant().setId(noteDTO.getEtudiant().getId());
            }
            if (noteDTO.getEnseignant() != null) {
                existing.getEnseignant().setId(noteDTO.getEnseignant().getId());
            }

            return noteMapper.toDto(noteRepository.save(existing));
        }
        return null;
    }

    @Override
    public List<NoteDTO> getAll() {
        return noteMapper.toDtoList(noteRepository.findAll());
    }

    @Override
    public NoteDTO getById(Long id) {
        return noteRepository.findById(id)
                .map(noteMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<NoteDTO> notes) {
        List<NoteEntity> entities = noteMapper.toEntityList(notes);
        noteRepository.deleteAll(entities);
    }
}
