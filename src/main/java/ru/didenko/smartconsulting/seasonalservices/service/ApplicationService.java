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

    public List<Application> getAllApplicationsOfUser(Long userId) {
        try {
            userService.getOneById(userId);
        } catch (Exception ex) {
            throw new IncorrectIdException("No such user with id " + userId + " exists");
        }
        return repository.getAllByUserId(userId);
    }

    public List<Application> getConfirmedApplicationsOfUser(Long userId) {
        try {
            userService.getOneById(userId);
        } catch (Exception ex) {
            throw new IncorrectIdException("No such user with id " + userId + " exists");
        }
        return repository.getConfirmedByUserId(userId);
    }

    public List<Application> getConfirmedApplications() {
        return repository.getAllByIsConfirmedOrderByCreatedWhen(true);
    }

    public List<Application> getNotConfirmedApplications() {
        return repository.getAllByIsConfirmedOrderByCreatedWhen(false);
    }

    public List<Application> getApplicationsToOneService(Long serviceId) {
        try {
            seasonalServicesService.getOneById(serviceId);
        } catch (Exception ex) {
            throw new IncorrectIdException("No such service with id " + serviceId + " exists");
        }
        return repository.findAllByServiceId(serviceId);
    }
}
