package ci.ada.monetabv2new.services.impl;


import ci.ada.monetabv2new.models.MessageEntity;
import ci.ada.monetabv2new.repositories.MessageRepository;
import ci.ada.monetabv2new.services.MessageService;
import ci.ada.monetabv2new.services.dto.MessageDTO;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;
import ci.ada.monetabv2new.services.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public MessageDTO save(MessageDTO messageDTO) {
        // S'assurer que la date est définie
        if (messageDTO.getDate() == null) {
            messageDTO.setDate(Instant.now());
        }
        MessageEntity messageEntity = messageRepository.save(messageMapper.toEntity(messageDTO));



        MessageEntity savedEntity = messageRepository.save(messageEntity);
        return messageMapper.toDto(savedEntity);
    }

    @Override
    public MessageDTO partialUpdate(MessageDTO messageDTO) {
        return null;
    }

    @Override
    public List<MessageDTO> getAll() {
        return messageRepository.findAll().stream().map(messageMapper::toDto).toList();
    }

    @Override
    public MessageDTO getById(Long id) {
        return messageRepository.findById(id).map(messageMapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);

    }

    @Override
    public void deleteAll(List<MessageDTO> d) {
        messageRepository.deleteAll(messageMapper.toEntityList(d));
    }




    @Override
    public List<MessageDTO> getConversationBetweenUsers(UserAccountDTO user1, UserAccountDTO user2) {
        List<MessageEntity> messages = messageRepository.findConversationBetweenUsers(
                user1.getId(), user2.getId()
        );

        return messages.stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getMessagesByUser(UserAccountDTO user) {
        List<MessageEntity> messages = messageRepository.findMessagesByUser(user.getId());

        return messages.stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }



    // Méthode utilitaire pour obtenir les partenaires de conversation
    public List<UserAccountDTO> getConversationPartners(UserAccountDTO user) {
        List<MessageEntity> messages = messageRepository.findMessagesByUser(user.getId());

        return messages.stream()
                .map(message -> {
                    // Retourner l'autre utilisateur de la conversation
                    if (message.getSender().getId().equals(user.getId())) {
                        return message.getReceiver();
                    } else {
                        return message.getSender();
                    }
                })
                .distinct()
                .map(entity -> modelMapper.map(entity, UserAccountDTO.class))
                .collect(Collectors.toList());
    }
}
