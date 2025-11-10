package ci.ada.monetabv2new.repositories;

import ci.ada.monetabv2new.models.MatiereEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MatiereRepository extends JpaRepository<MatiereEntity, Long> {
    Optional<MatiereEntity> findByCode(String code);
    Optional<MatiereEntity> findByNom(String nom);
    boolean existsByCode(String code);
}