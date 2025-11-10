package ci.ada.monetabv2new.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant date;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private UserAccountEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private UserAccountEntity receiver;


}
