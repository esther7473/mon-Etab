package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.AbsenceEntity;
import ci.ada.monetabv2new.models.CoursEntity;
import ci.ada.monetabv2new.models.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AbsenceRepository extends JpaRepository<AbsenceEntity, Long> {
    List<AbsenceEntity> findByEtudiant(StudentEntity etudiant);
    List<AbsenceEntity> findByCours(CoursEntity cours);
    Optional<AbsenceEntity> findByEtudiantAndCours(StudentEntity etudiant, CoursEntity cours);
    Long countByEtudiantAndJustifiee(StudentEntity etudiant, Boolean justifiee);

    void deleteByCoursAndEtudiant(CoursEntity coursEntity,StudentEntity studentEntity);


}
