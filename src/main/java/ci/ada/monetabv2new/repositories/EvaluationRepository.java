package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<EvaluationEntity, Long> {
    List<EvaluationEntity> findByEnseignantId(Long enseignantId);
    List<EvaluationEntity> findByClasseId(Long classeId);
    List<EvaluationEntity> findByDateRemiseBetween(LocalDate startDate, LocalDate endDate);
}