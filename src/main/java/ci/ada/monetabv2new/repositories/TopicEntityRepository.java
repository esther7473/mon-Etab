package ci.ada.monetabv2new.repositories;

import ci.ada.monetabv2new.models.forum.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicEntityRepository extends JpaRepository<TopicEntity, Long> {
}
