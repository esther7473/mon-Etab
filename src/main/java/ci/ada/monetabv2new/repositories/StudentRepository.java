package ci.ada.monetabv2new.repositories;

import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.StudentEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByMatricule(String matricule);
    List<StudentEntity> findByClassEntityId(Long classEntityId);
    boolean existsByMatricule(String matricule);
    Long countByClassEntity(ClassEntity classEntity);
    Optional<StudentEntity> findByUserAccountEntity(UserAccountEntity userAccountEntity);

    List<StudentEntity> findByParent_Id(Long parentId);
}
