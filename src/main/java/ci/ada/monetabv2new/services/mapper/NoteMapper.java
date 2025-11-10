package ci.ada.monetabv2new.services.mapper;


import ci.ada.monetabv2new.models.NoteEntity;
import ci.ada.monetabv2new.services.dto.NoteDTO;
import org.springframework.stereotype.Service;

@Service
public interface NoteMapper extends Mapper<NoteDTO, NoteEntity> {
}
