package ru.didenko.smartconsulting.seasonalservices.service;

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


/**
 * Class-service with main logic for Seasonal services
 */
@Service
public class SeasonalServicesService extends GenericService<SeasonalService> {

    private final SeasonalServiceRepository repository;

    public SeasonalServicesService(SeasonalServiceRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * @return List of all Services
     */
    @Override
    public List<SeasonalService> getList() {
        return repository.findAllByOrderByDateStart();
    }

    /**
     * Method adds new Service to DB
     *
     * @param object - Service object
     * @return added to DB object
     */
    @Override
    public SeasonalService create(SeasonalService object) {
        object.setCreatedWhen(LocalDateTime.now());
        object.setCreatedBy("Manager");
        setDeletedAndUpdatedNull(object);
        return super.create(object);
    }

    /**
     * Method updates Service in DB
     *
     * @param object - Service object to Update
     * @return updated in DB object
     */
    @Override
    public SeasonalService update(SeasonalService object) {
        setCreatedAndDeleted(object.getId(), object);
        object.setUpdatedBy("Manager");
        object.setUpdatedWhen(LocalDateTime.now());
        return super.update(object);
    }

    /**
     * Transactional method that trying to decrease balance of one Service
     *
     * @param id - Service id
     * @throws SpentLimitException after request when balance of service is 0
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public void getOneService(Long id) throws SpentLimitException {
        try {
            repository.getTheService(id);
        } catch (Exception ex) {
            throw new SpentLimitException("Limit of service is exhausted");
        }
    }

    /**
     * Method returns services if current Date is between start and expired dates of Service.
     * Also checks balance of each service
     *
     * @return List of allowed at current time services
     */
    public List<SeasonalService> getAllowedServices() {
        return repository.getAllowedServices(LocalDate.now());
    }
}
