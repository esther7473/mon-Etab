package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.MessageEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.MessageDTO;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageMapperImpl implements MessageMapper {
    private final ModelMapper modelMapper;

    @Override
    public MessageDTO toDto(MessageEntity messageEntity) {

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(messageDTO.getId());
        messageDTO.setDate(messageDTO.getDate());
        messageDTO.setMessage(messageDTO.getMessage());
        messageDTO.setSender(modelMapper.map(messageEntity.getSender(), UserAccountDTO.class));
        messageDTO.setReceiver(modelMapper.map(messageEntity.getReceiver(), UserAccountDTO.class));

        return messageDTO;
    }

    @Override
    public MessageEntity toEntity(MessageDTO messageDTO) {

        MessageEntity messageEntity = new  MessageEntity();

        messageEntity.setId(messageDTO.getId());
        messageEntity.setDate(messageDTO.getDate());
        messageEntity.setMessage(messageDTO.getMessage());
        messageEntity.setSender(modelMapper.map(messageDTO.getSender(), UserAccountEntity.class));
        messageEntity.setReceiver(modelMapper.map(messageDTO.getReceiver(), UserAccountEntity.class));

        return messageEntity;
    }

    @Override
    public List<MessageDTO> toDtoList(List<MessageEntity> messageEntities)
    {
        return messageEntities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MessageEntity> toEntityList(List<MessageDTO> messageDTOS) {

        return messageDTOS.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
