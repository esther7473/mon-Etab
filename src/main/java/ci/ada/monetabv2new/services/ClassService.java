package ci.ada.monetabv2new.services;

import ci.ada.monetabv2new.services.dto.ClassDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService extends CRUDService<ClassDTO> {
    List<ClassDTO> getClassesByTeacherId(Long teacherId);
    
    ClassDTO addTeacherToClass(Long classId, Long teacherId);
    ClassDTO removeTeacherFromClass(Long classId, Long teacherId);
    List<ClassDTO> getClassesWithTeachers();
}
