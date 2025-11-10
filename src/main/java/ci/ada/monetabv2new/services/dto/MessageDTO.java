package ci.ada.monetabv2new.services.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MessageDTO {

    private Long id;

    @NotNull(message = "La date est obligatoire")
    private Instant date;

    private String message;

    @NotNull(message = "L'emetteur est obligatoire")
    private UserAccountDTO sender;

    @NotNull(message = "Le destinataire est obligatoire")
    private UserAccountDTO receiver;

}
