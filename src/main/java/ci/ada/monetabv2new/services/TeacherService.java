package ci.ada.monetabv2new.services;



import ci.ada.monetabv2new.models.UserAccountEntity;
import ci.ada.monetabv2new.services.dto.TeacherDTO;

import java.util.List;
import java.util.Optional;

public interface TeacherService extends CRUDService<TeacherDTO> {
    // Méthodes spécifiques aux enseignants
    Optional<TeacherDTO> findByLogin(UserAccountEntity login);
    //TeacherDataDTO getTeacherWithDetails(Long id);
    
    // Méthodes pour gérer les relations avec les classes
    TeacherDTO addClassToTeacher(Long teacherId, Long classId);
    TeacherDTO removeClassFromTeacher(Long teacherId, Long classId);
    List<TeacherDTO> getTeachersByClassId(Long classId);

    //TeacherDTO findByUserAccount(UserAccountDTO user);


    // Méthodes avec DTOs spécifiques
    /*TeacherDTO saveFromRequest(TeacherRequestDTO requestDTO);
    TeacherDTO updateFromRequest(TeacherRequestDTO requestDTO);

     */
}
