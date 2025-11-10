package ci.ada.monetabv2new.services;




import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService extends CRUDService<StudentDTO> {
    Optional<StudentDTO> findByUserAccount(UserAccountEntity userAccount);
    List<StudentDTO> getStudentsByClassId(Long classId);

    Optional<StudentDTO> findByLogin(UserAccountEntity login);
    List<StudentDTO> getStudentsByParentId(Long parentId);

    // Méthodes avec DTOs spécifiques
    /*
    StudentDTO saveFromRequest(StudentRequestDTO requestDTO);
    StudentDTO updateFromRequest(StudentRequestDTO requestDTO);

     */
}
