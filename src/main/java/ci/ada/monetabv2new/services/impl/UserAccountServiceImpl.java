package ci.ada.monetabv2new.services.impl;

import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.repositories.UserAccountRepository;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserAccountDTO saveWithEncodedPassword(UserAccountDTO userDTO) {
        UserAccountEntity entity = modelMapper.map(userDTO, UserAccountEntity.class);
        if (entity.getPassword() != null) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        }
        entity = userAccountRepository.save(entity);
        return modelMapper.map(entity, UserAccountDTO.class);
    }

    @Override
    public UserAccountDTO findByLoginAndPassword(UserAccountDTO userDTO) {
        Optional<UserAccountEntity> optional = userAccountRepository.findByUsername(userDTO.getUsername());
        if (optional.isPresent() && passwordEncoder.matches(userDTO.getPassword(), optional.get().getPassword())) {
            return modelMapper.map(optional.get(), UserAccountDTO.class);
        }
        return null;
    }

    @Override
    public Optional<UserAccountDTO> findByLogin(String login) {
        return userAccountRepository.findByUsername(login)
                .map(user -> modelMapper.map(user, UserAccountDTO.class));
    }

    @Override
    public UserAccountDTO save(UserAccountDTO userDTO) {
        UserAccountEntity entity = modelMapper.map(userDTO, UserAccountEntity.class);
        entity = userAccountRepository.save(entity);
        return modelMapper.map(entity, UserAccountDTO.class);
    }

    @Override
    public UserAccountDTO partialUpdate(UserAccountDTO userDTO) {
        if (Objects.isNull(userDTO.getId())) {
            throw new IllegalArgumentException("User ID not exist");
        }

        return userAccountRepository.findById(userDTO.getId())
                .map(entity -> {
//                    if (userDTO.getEmail() != null) entity.setEmail(userDTO.getEmail());
                    if (userDTO.getUsername() != null) entity.setUsername(userDTO.getUsername());
                    if (userDTO.getPassword() != null)
                        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    if (userDTO.getRole() != null) entity.setRole(userDTO.getRole());

                    entity = userAccountRepository.save(entity);
                    return modelMapper.map(entity, UserAccountDTO.class);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<UserAccountDTO> getAll() {
        return userAccountRepository.findAll().stream()
                .map(entity -> modelMapper.map(entity, UserAccountDTO.class))
                .toList();
    }

    @Override
    public UserAccountDTO getById(Long id) {
        return userAccountRepository.findById(id)
                .map(entity -> modelMapper.map(entity, UserAccountDTO.class))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public void delete(Long id) {
        userAccountRepository.deleteById(id);
    }

    @Override
    public void deleteAll(List<UserAccountDTO> users) {
        List<UserAccountEntity> entities = users.stream()
                .map(dto -> modelMapper.map(dto, UserAccountEntity.class))
                .toList();
        userAccountRepository.deleteAll(entities);
    }
}
