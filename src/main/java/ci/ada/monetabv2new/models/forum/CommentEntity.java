package ci.ada.monetabv2new.models.forum;

import ci.ada.monetabv2new.models.UserAccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private TopicEntity topic;
    @ManyToOne
    private UserAccountEntity author;
}
