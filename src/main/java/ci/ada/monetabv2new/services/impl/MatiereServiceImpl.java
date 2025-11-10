.
package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.MatiereEntity;
import ci.ada.monetabv2new.repositories.MatiereRepository;
import ci.ada.monetabv2new.services.MatiereService;
import ci.ada.monetabv2new.services.dto.MatiereDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MatiereServiceImpl implements MatiereService {
    private final MatiereRepository matiereRepository;
    private final ModelMapper modelMapper;

    @Override
    public MatiereDTO save(MatiereDTO matiereDTO) {
        MatiereEntity matiereEntity = modelMapper.map(matiereDTO, MatiereEntity.class);
        matiereEntity = matiereRepository.save(matiereEntity);
        return modelMapper.map(matiereEntity, MatiereDTO.class);
    }

    @Override
    public MatiereDTO partialUpdate(MatiereDTO matiereDTO) {
        if (Objects.isNull(matiereDTO.getId())) {
            throw new IllegalArgumentException("Matiere ID not exist");
        }

        return matiereRepository.findById(matiereDTO.getId())
                .map(entity -> {
                    if (Objects.nonNull(matiereDTO.getNom())) {
                        entity.setNom(matiereDTO.getNom());
                    }
                    if (Objects.nonNull(matiereDTO.getCode())) {
                        entity.setCode(matiereDTO.getCode());
                    }
                    entity = matiereRepository.save(entity);
                    return modelMapper.map(entity, MatiereDTO.class);
                })
                .orElseThrow(() -> new IllegalArgumentException("Matiere not found"));
    }
    @Override
    public List<MatiereDTO> getAll() {
        return matiereRepository.findAll().stream().map(matiereEntity -> modelMapper.map(matiereEntity, MatiereDTO.class)).toList();
    }

    @Override
    public MatiereDTO getById(Long id) {
        return matiereRepository.findById(id)
                .map(matiereEntity -> modelMapper.map(matiereEntity, MatiereDTO.class))
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        matiereRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<MatiereDTO> d) {

    }
}
