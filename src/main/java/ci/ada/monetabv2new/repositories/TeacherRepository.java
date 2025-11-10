package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.MatiereEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity, Long> {
    List<TeacherEntity> findByMatiere(MatiereEntity matiere);
    List<TeacherEntity> findByIsVacant(Boolean isVacant);
    Optional<TeacherEntity> findByUserAccountEntity(UserAccountEntity userAccountEntity);

}