package ci.ada.monetabv2new.services.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassDTO {
    private Long id;

    @NotBlank(message = "Le libellé de la classe est obligatoire")
    private String label;

    @Min(value = 1, message = "Le nombre maximum d'étudiants doit être positif")
    private Integer maxStudent;

    private List<Long> students;

    private List<Long> teachers;


}
