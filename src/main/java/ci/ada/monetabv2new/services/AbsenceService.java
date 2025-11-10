package ci.ada.monetabv2new.services;


import ci.ada.monetabv2new.services.dto.AbsenceDTO;
import ci.ada.monetabv2new.services.dto.CoursDTO;
import ci.ada.monetabv2new.services.dto.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AbsenceService extends CRUDService<AbsenceDTO> {
    List<AbsenceDTO> getAllByStudentId(StudentDTO student);
    List<AbsenceDTO> getAllByCoursId(CoursDTO cours);
    void deleteByCoursAndStudent(CoursDTO coursDTO, StudentDTO studentDTO);
}
