package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.AdminEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.repositories.AdminRepository;
import ci.ada.monetabv2new.services.AdminService;
import ci.ada.monetabv2new.services.dto.AdminDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    @Override
    public AdminDTO save(AdminDTO adminDTO) {
        AdminEntity adminEntity = modelMapper.map(adminDTO, AdminEntity.class);
        AdminDTO admin = modelMapper.map(adminRepository.save(adminEntity), AdminDTO.class);
        return admin;
    }

    @Override
    public AdminDTO partialUpdate(AdminDTO adminDTO) {
        return null;
    }

    @Override
    public List<AdminDTO> getAll() {
        return adminRepository.findAll().stream().map(adminEntity -> modelMapper.map(adminEntity, AdminDTO.class)).toList();
    }

    @Override
    public AdminDTO getById(Long id) {
        return adminRepository.findById(id)
                .map(adminEntity -> modelMapper.map(adminEntity, AdminDTO.class))
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<AdminDTO> d) {

    }

    @Override
    public AdminDTO getByLogin(UserAccountEntity login) {
        return modelMapper.map(adminRepository.findByUserAccountEntity(login), AdminDTO.class);
    }
}
