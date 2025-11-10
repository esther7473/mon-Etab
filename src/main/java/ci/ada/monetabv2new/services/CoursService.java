package ci.ada.monetabv2new.services;

import ci.ada.monetabv2new.services.dto.CoursDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CoursService extends CRUDService<CoursDTO> {
    List<CoursDTO> findByTeacherId(Long teacherId);
    List<CoursDTO> findByClassId(Long classId);
    List<CoursDTO> findByDate(LocalDate date);
    List<CoursDTO> findConflictingSalle(String salle, LocalDate date, LocalTime heureDebut, LocalTime heureFin);
    List<CoursDTO> findConflictingTeacher(Long enseignantId, LocalDate date, LocalTime heureDebut, LocalTime heureFin);
    List<CoursDTO> findByTeacherAndDateRange(Long teacherId, LocalDate startDate, LocalDate endDate);
    List<CoursDTO> findByClassAndTeacher(Long teacherId,Long classId);

    // Méthodes de validation métier
  /*  boolean hasConflictingSalle(CoursDTO coursDTO);
    boolean hasConflictingTeacher(CoursDTO coursDTO);

   */
}
