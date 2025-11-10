package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.EvaluationEntity;
import ci.ada.monetabv2new.models.NoteEntity;
import ci.ada.monetabv2new.models.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {
    List<NoteEntity> findByEtudiant_Id(Long studentId);
    List<NoteEntity> findByEvaluation_Id(Long evaluationId);
    Optional<NoteEntity> findByEtudiantAndEvaluation(StudentEntity etudiant, EvaluationEntity evaluation);

    @Query("SELECT AVG(n.valeur) FROM NoteEntity n WHERE n.etudiant = :etudiant")
    Double calculateAverageByStudent(StudentEntity etudiant);
}