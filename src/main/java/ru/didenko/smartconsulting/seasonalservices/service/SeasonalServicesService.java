package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;
import ru.didenko.smartconsulting.seasonalservices.repository.SeasonalServiceRepository;

import java.time.LocalDateTime;

@Service
public class SeasonalServicesService extends GenericService<SeasonalService> {

    public SeasonalServicesService(SeasonalServiceRepository repository) {
        super(repository);
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
}
