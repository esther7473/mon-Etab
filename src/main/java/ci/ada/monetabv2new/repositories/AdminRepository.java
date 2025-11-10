package ci.ada.monetabv2new.repositories;


import ci.ada.monetabv2new.models.AdminEntity;
import ci.ada.monetabv2new.models.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    Optional<AdminEntity> findByUserAccountEntity(UserAccountEntity userAccountEntity);

}
