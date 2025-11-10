package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.ParentEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, Long> {
    Optional<ParentEntity> findByUserAccount(UserAccountEntity userAccountEntity);

}
