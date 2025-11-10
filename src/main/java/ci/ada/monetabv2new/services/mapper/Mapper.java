package ci.ada.monetabv2new.services.mapper;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Mapper <DTO, ENTITY>{

    DTO toDto(ENTITY entity);
    ENTITY toEntity(DTO dto);
    List<DTO> toDtoList(List<ENTITY> entityList);
    List<ENTITY> toEntityList(List<DTO> dtoList);
}
