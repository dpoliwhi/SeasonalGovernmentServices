package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.repository.GenericRepository;

import java.time.LocalDateTime;

@Service
public class ApplicationService extends GenericService<Application> {

    private final UserService userService;
    private final SeasonalServicesService seasonalServicesService;

    public ApplicationService(
            GenericRepository<Application> repository,
            UserService userService,
            SeasonalServicesService servicesService) {
        super(repository);
        this.userService = userService;
        this.seasonalServicesService = servicesService;
    }

    @Override
    public Application create(Application object) {
        object.setCreatedWhen(LocalDateTime.now());
        object.setCreatedBy(object.getUser().getEmail());
        setDeletedAndUpdatedNull(object);
        return super.create(object);
    }

    @Override
    public Application update(Application object) {
        setCreatedAndDeleted(object.getId(), object);
        object.setUpdatedBy("Manager");
        object.setUpdatedWhen(LocalDateTime.now());
        return super.update(object);
    }
}
