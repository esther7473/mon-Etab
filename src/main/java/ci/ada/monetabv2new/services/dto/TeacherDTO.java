package ci.ada.monetabv2new.services.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TeacherDTO extends PersonDTO{
    private Long id;

    private Boolean isVacant;
    private MatiereDTO matiere;
    private UserAccountDTO userAccount;


}