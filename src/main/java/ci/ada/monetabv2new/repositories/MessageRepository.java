package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.MessageEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findBySenderOrReceiver(UserAccountEntity sender, UserAccountEntity receiver);

    @Query("""
        SELECT m FROM MessageEntity m\s
        WHERE (m.sender.id = :user1Id AND m.receiver.id = :user2Id)\s
           OR (m.sender.id = :user2Id AND m.receiver.id = :user1Id)
        ORDER BY m.date ASC
       \s""")
    List<MessageEntity> findConversationBetweenUsers(@Param("user1Id") Long user1Id,
                                                     @Param("user2Id") Long user2Id);

    // Messages où l'utilisateur est impliqué (envoyé ou reçu)
    @Query("""
        SELECT m FROM MessageEntity m 
        WHERE m.sender.id = :userId OR m.receiver.id = :userId
        ORDER BY m.date DESC
        """)
    List<MessageEntity> findMessagesByUser(@Param("userId") Long userId);
}
