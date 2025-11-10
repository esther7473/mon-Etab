package ci.ada.monetabv2new.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CRUDService<D> {

    D save(D d);
    D partialUpdate(D d);
    List<D> getAll();
    D getById(Long id);
    void delete(Long id);
    void deleteAll(List<D> d);

}