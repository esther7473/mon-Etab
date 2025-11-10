package ci.ada.monetabv2new.services;

import ci.ada.monetabv2new.models.ParentEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.repositories.ParentRepository;
import ci.ada.monetabv2new.services.dto.ParentDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final ModelMapper modelMapper;

    @Override
    public ParentDTO save(ParentDTO parentDTO) {
        return modelMapper.map(parentRepository.save(modelMapper.map(parentDTO, ParentEntity.class)),ParentDTO.class);
    }

    @Override
    public ParentDTO partialUpdate(ParentDTO parentDTO) {
        return null;
    }

    @Override
    public List<ParentDTO> getAll() {
        return parentRepository.findAll().stream().map(p->modelMapper.map(p,ParentDTO.class)).toList();
    }

    @Override
    public ParentDTO getById(Long id) {
        return modelMapper.map(parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent not found with id " + id)),ParentDTO.class);
    }

    @Override
    public void delete(Long id) {
        parentRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<ParentDTO> d) {

    }

    @Override
    public ParentDTO getByLogin(UserAccountEntity login) {
        return modelMapper.map(parentRepository.findByUserAccount(login), ParentDTO.class);

    }
}
