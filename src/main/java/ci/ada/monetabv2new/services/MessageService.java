package ci.ada.monetabv2new.services;


import ci.ada.monetabv2new.services.dto.MessageDTO;
import ci.ada.monetabv2new.services.dto.UserAccountDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService extends CRUDService<MessageDTO> {
    List<MessageDTO> getMessagesByUser(UserAccountDTO user);
    List<MessageDTO> getConversationBetweenUsers(UserAccountDTO user1, UserAccountDTO user2);
    MessageDTO save(MessageDTO messageDTO);
    List<UserAccountDTO> getConversationPartners(UserAccountDTO user);
}
