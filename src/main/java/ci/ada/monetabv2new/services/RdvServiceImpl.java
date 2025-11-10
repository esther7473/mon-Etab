package ci.ada.monetabv2new.services;


import ci.ada.monetabv2new.models.RdvEntity;
import ci.ada.monetabv2new.repositories.RdvRepository;
import ci.ada.monetabv2new.services.dto.RdvDTO;
import ci.ada.monetabv2new.services.mapper.RdvMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RdvServiceImpl implements RdvService {

    private final RdvRepository rdvRepository;
    private final RdvMapper rdvMapper;

    @Override
    public RdvDTO save(RdvDTO rdvDTO) {
        RdvEntity rdvEntity = rdvMapper.toEntity(rdvDTO);
        RdvEntity saved = rdvRepository.save(rdvEntity);
        return rdvMapper.toDto(saved);
    }

    @Override
    public RdvDTO partialUpdate(RdvDTO rdvDTO) {
        if (rdvDTO.getId() == null) {
            throw new IllegalArgumentException("L'ID est requis pour mettre à jour un rendez-vous");
        }

        Optional<RdvEntity> optionalRdv = rdvRepository.findById(rdvDTO.getId());
        if (optionalRdv.isEmpty()) {
            throw new IllegalArgumentException("Rendez-vous introuvable avec l'id " + rdvDTO.getId());
        }

        RdvEntity rdvEntity = optionalRdv.get();

       
        if (rdvDTO.getDate() != null) rdvEntity.setDate(rdvDTO.getDate());
        if (rdvDTO.getHeureDebut() != null) rdvEntity.setHeureDebut(rdvDTO.getHeureDebut());
        if (rdvDTO.getHeureFin() != null) rdvEntity.setHeureFin(rdvDTO.getHeureFin());
        if (rdvDTO.getMotif() != null) rdvEntity.setMotif(rdvDTO.getMotif());
        if (rdvDTO.getStatut() != null) rdvEntity.setStatut(rdvDTO.getStatut());
        if (rdvDTO.getStudent() != null) rdvEntity.setStudent(rdvMapper.toEntity(rdvDTO).getStudent());
        if (rdvDTO.getTeacher() != null) rdvEntity.setTeacher(rdvMapper.toEntity(rdvDTO).getTeacher());

        RdvEntity updated = rdvRepository.save(rdvEntity);
        return rdvMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RdvDTO> getAll() {
        return rdvMapper.toDtoList(rdvRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RdvDTO getById(Long id) {
        return rdvRepository.findById(id)
                .map(rdvMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Rendez-vous introuvable avec l'id " + id));
    }

    @Override
    public void delete(Long id) {
        if (!rdvRepository.existsById(id)) {
            throw new IllegalArgumentException("Impossible de supprimer : rendez-vous introuvable avec l'id " + id);
        }
        rdvRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<RdvDTO> rdvDTOS) {
        List<RdvEntity> entities = rdvMapper.toEntityList(rdvDTOS);
        rdvRepository.deleteAll(entities);
    }

    @Override
    public List<RdvDTO> getRdvsByTeacher(Long id) {
        return List.of();
    }

    @Override
    public List<RdvDTO> getRdvsByParent(Long id) {
        return List.of();
    }

    @Override
    public List<RdvDTO> getRdvsByStudent(Long id) {
        return rdvRepository.findAllByStudent_Id(id).stream().map(rdvMapper::toDto).toList();
    }
}
