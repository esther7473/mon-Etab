package ci.ada.monetabv2new.repositories;

import ci.ada.monetabv2new.models.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByLabel(String label);
    boolean existsByLabel(String label);
    @Query("SELECT c FROM ClassEntity c JOIN c.teachers t WHERE t.id = :teacherId")
    List<ClassEntity> findAllByTeacherId(@Param("teacherId") Long teacherId);

}