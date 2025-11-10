package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.ClassEntity;
import ci.ada.monetabv2new.models.CoursEntity;
import ci.ada.monetabv2new.models.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<CoursEntity, Long> {
    List<CoursEntity> findByEnseignant(TeacherEntity enseignant);
    List<CoursEntity> findByClasse(ClassEntity classe);
    List<CoursEntity> findByDateCours(LocalDate dateCours);
    List<CoursEntity> findByEnseignantAndClasse(TeacherEntity enseignant,ClassEntity classe);

    @Query("SELECT c FROM CoursEntity c WHERE c.salle = :salle AND c.dateCours = :date AND " +
            "((c.heureDebut <= :heureDebut AND c.heureFin > :heureDebut) OR " +
            "(c.heureDebut < :heureFin AND c.heureFin >= :heureFin))")
    List<CoursEntity> findConflictingSalle(String salle, LocalDate date, LocalTime heureDebut, LocalTime heureFin);

    @Query("SELECT c FROM CoursEntity c WHERE c.enseignant = :enseignant AND c.dateCours = :date AND " +
            "((c.heureDebut <= :heureDebut AND c.heureFin > :heureDebut) OR " +
            "(c.heureDebut < :heureFin AND c.heureFin >= :heureFin))")
    List<CoursEntity> findConflictingTeacher(TeacherEntity enseignant, LocalDate date, LocalTime heureDebut, LocalTime heureFin);

    List<CoursEntity> findByEnseignantAndDateCoursBetweenOrderByDateCoursAscHeureDebutAsc(
            TeacherEntity enseignant,
            LocalDate startDate,
            LocalDate endDate);

    List<CoursEntity> findByEnseignantAndDateCoursBetween(TeacherEntity enseignant, LocalDate startOfWeek, LocalDate endOfWeek);
}