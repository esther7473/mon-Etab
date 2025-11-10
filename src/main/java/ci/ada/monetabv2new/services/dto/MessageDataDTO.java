package ci.ada.monetabv2new.services.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDataDTO {
    private Long id;
    private String message;
    private String sender;
    private String receiver;
    private String date;
    private String time;
    private String status;
}
