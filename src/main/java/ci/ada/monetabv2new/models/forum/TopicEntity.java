package ci.ada.monetabv2new.models.forum;


import ci.ada.monetabv2new.models.UserAccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private CategoryEntity category;
    @ManyToOne
    private UserAccountEntity author; // Teacher, Student, Admin, Parent
}
