package ci.ada.monetabv2new.repositories;

import ci.ada.monetabv2new.models.RdvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RdvRepository extends JpaRepository<RdvEntity, Long> {
    List<RdvEntity> findAllByStudent_Id(Long studentId);
}
