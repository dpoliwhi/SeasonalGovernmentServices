package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.didenko.smartconsulting.seasonalservices.exceptions.ApplicationFailException;
import ru.didenko.smartconsulting.seasonalservices.exceptions.IncorrectIdException;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.repository.ApplicationRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class-service with main logic for applying applications
 */
@Service
public class ApplicationService extends GenericService<Application> {

    private final SeasonalServicesService seasonalServicesService;
    private final UserService userService;
    private final ApplicationRepository repository;

    public ApplicationService(
            ApplicationRepository repository,
            SeasonalServicesService servicesService, UserService userService) {
        super(repository);
        this.seasonalServicesService = servicesService;
        this.repository = repository;
        this.userService = userService;
    }

    /**
     * Transactional method to create new application<p>
     * Checks if such user or service is doesn't exist<p>
     * Create new application regardless of whether it is possible to receive the service or not<p>
     * Tries to get this service<p>
     * Sends the email with approval or rejection of the application
     *
     * @param object - object with main info about user and with id of service
     * @return created application
     * @throws ApplicationFailException if such user or service is doesn't exist or if the service is not available due to dates
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public Application create(Application object) throws ApplicationFailException {
        object.setCreatedWhen(LocalDateTime.now());
        checkUserAndService(object);
        object.setCreatedBy(object.getUser().getLogin());
        setDeletedAndUpdatedNull(object);
        Application result = super.create(object);
        try {
            seasonalServicesService.getOneService(object.getService().getId());
            sendSuccessEmail(object.getUser().getEmail(), object.getService().getName(), object.getService().getDescription());
            repository.toConfirmApplication(result.getId());
        } catch (Exception ex) {
            sendFailEmail(object.getUser().getEmail(), object.getService().getName());
        }
        return result;
    }

    void checkUserAndService(Application application) throws ApplicationFailException {
        if (Objects.isNull(application.getService())) {
            throw new ApplicationFailException("This seasonal service not found");
        }
        if (Objects.isNull(application.getUser())) {
            throw new ApplicationFailException("User not found");
        }
        if (application.getCreatedWhen().isAfter(application.getService().getDateExpiration().atStartOfDay()) ||
                application.getCreatedWhen().isBefore(application.getService().getDateStart().atStartOfDay())) {
            throw new ApplicationFailException("This service is not available due to dates");
        }
    }

    /**
     * Method calls a main method of sending information with custom message
     *
     * @param email              of client
     * @param serviceName        - name of the seasonal service
     * @param serviceDescription - description of the seasonal service
     */
    private void sendSuccessEmail(String email, String serviceName, String serviceDescription) {
        sendEmailMessage(email,
                "Вам одобрена госудаственная услуга \"" +
                        serviceName + "\"\n" +
                        "Описание услуги: \"" +
                        serviceDescription + "\"");
    }

    private void sendFailEmail(String email, String serviceName) {
        sendEmailMessage(email,
                "К сожалению вам отказано в предоставлении госудаственной услуги \"" +
                        serviceName + "\"\n" +
                        "Возможно, квота по данной услуге исчерпана.\n" +
                        "Приносим свои извенения.");
    }

    /**
     * Updates the Application
     *
     * @param object for update
     * @return updated application from DB
     */
    @Override
    public Application update(Application object) {
        setCreatedAndDeleted(object.getId(), object);
        object.setUpdatedBy("Admin");
        object.setUpdatedWhen(LocalDateTime.now());
        return super.update(object);
    }

    /**
     * Method creates Info Messages for response with information about application and service
     */
    public Map<String, Object> infoResponse(Application application) {
        Map<String, Object> response = new HashMap<>();
        response.put("Id", application.getId());
        response.put("Name of service", application.getService().getName());
        response.put("Description of service", application.getService().getDescription());
        return response;
    }

    /**
     * Method returns all Applications of user ordered by CreatedWhen field
     *
     * @param userId - id of user
     * @return List of Applications
     */
    public List<Application> getAllApplicationsOfUser(Long userId) {
        try {
            userService.getOneById(userId);
        } catch (Exception ex) {
            throw new IncorrectIdException("No such user with id " + userId + " exists");
        }
        return repository.getAllByUserId(userId);
    }


    /**
     * Method returns approved Applications of user ordered by CreatedWhen field
     *
     * @param userId - id of user
     * @return List of Applications
     */
    public List<Application> getConfirmedApplicationsOfUser(Long userId) {
        try {
            userService.getOneById(userId);
        } catch (Exception ex) {
            throw new IncorrectIdException("No such user with id " + userId + " exists");
        }
        return repository.getConfirmedByUserId(userId);
    }

    /**
     * Method returns all approved Applications ordered by CreatedWhen field
     *
     * @return List of Applications
     */
    public List<Application> getConfirmedApplications() {
        return repository.getAllByIsConfirmedOrderByCreatedWhen(true);
    }

    /**
     * Method returns all rejected Applications ordered by CreatedWhen field
     *
     * @return List of Applications
     */
    public List<Application> getNotConfirmedApplications() {
        return repository.getAllByIsConfirmedOrderByCreatedWhen(false);
    }

    /**
     * Method returns all Applications of one service ordered by confirmation and CreatedWhen field
     *
     * @param serviceId - id of service
     * @return List of Applications
     */
    public List<Application> getApplicationsToOneService(Long serviceId) {
        try {
            seasonalServicesService.getOneById(serviceId);
        } catch (Exception ex) {
            throw new IncorrectIdException("No such service with id " + serviceId + " exists");
        }
        return repository.findAllByServiceId(serviceId);
    }
}
