package ru.didenko.smartconsulting.seasonalservices.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.didenko.smartconsulting.seasonalservices.exceptions.SpentLimitException;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;
import ru.didenko.smartconsulting.seasonalservices.repository.SeasonalServiceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class SeasonalServicesService extends GenericService<SeasonalService> {

    private final SeasonalServiceRepository repository;

    public SeasonalServicesService(SeasonalServiceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<SeasonalService> getList() {
        return repository.findAllByOrderByDateStart();
    }

    @Override
    public SeasonalService create(SeasonalService object) {
        object.setCreatedWhen(LocalDateTime.now());
        object.setCreatedBy("Manager");
        setDeletedAndUpdatedNull(object);
        return super.create(object);
    }

    @Override
    public SeasonalService update(SeasonalService object) {
        setCreatedAndDeleted(object.getId(), object);
        object.setUpdatedBy("Manager");
        object.setUpdatedWhen(LocalDateTime.now());
        return super.update(object);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void getOneService(Long id) throws SpentLimitException {
        try {
            repository.getTheService(id);
        } catch (Exception ex) {
            throw new SpentLimitException("Limit of service is exhausted");
        }
    }

    public List<SeasonalService> getAllowedServices() {
        return repository.getAllowedServices(LocalDate.now());
    }
}
