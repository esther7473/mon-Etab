package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.MessageEntity;
import ci.ada.monetabv2new.services.dto.MessageDTO;
import org.springframework.stereotype.Service;

@Service
public interface MessageMapper extends Mapper<MessageDTO, MessageEntity>{
}
