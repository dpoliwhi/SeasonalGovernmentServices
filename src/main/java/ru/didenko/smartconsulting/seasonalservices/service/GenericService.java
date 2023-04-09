package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.model.GenericModel;
import ru.didenko.smartconsulting.seasonalservices.repository.GenericRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class GenericService<T extends GenericModel> {

    private final GenericRepository<T> repository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GenericService(GenericRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> getList() {
        return repository.findAll();
    }

    public T getOneById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public T create(T object) {
        object.setCreatedWhen(LocalDateTime.now());
        setDeletedAndUpdatedNull(object);
        return repository.save(object);
    }

    public T update(T object) {
        object.setUpdatedWhen(LocalDateTime.now());
        return repository.save(object);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    protected void setDeletedAndUpdatedNull(T object) {
        object.setDeleted(false);
        object.setDeletedWhen(null);
        object.setDeletedBy(null);
        object.setUpdatedBy(null);
        object.setUpdatedWhen(null);
    }

    public void setCreatedAndDeleted(Long sourceId, T destination) {
        T source = getOneById(sourceId);
        destination.setCreatedBy(source.getCreatedBy());
        destination.setCreatedWhen(source.getCreatedWhen());
        destination.setDeletedBy(source.getDeletedBy());
        destination.setDeletedWhen(source.getDeletedWhen());
        destination.setDeleted(source.isDeleted());
    }
}
