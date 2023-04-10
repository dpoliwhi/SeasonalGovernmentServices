package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.repository.ApplicationRepository;

import java.time.LocalDateTime;

@Service
public class ApplicationService extends GenericService<Application> {

    private final SeasonalServicesService seasonalServicesService;
    private final ApplicationRepository repository;

    public ApplicationService(
            ApplicationRepository repository,
            SeasonalServicesService servicesService) {
        super(repository);
        this.seasonalServicesService = servicesService;
        this.repository = repository;
    }

    @Override
    public Application create(Application object) {
        object.setCreatedWhen(LocalDateTime.now());
        object.setCreatedBy(object.getUser().getEmail());
        setDeletedAndUpdatedNull(object);
        Application result = super.create(object);

        // TODO на юзера записываются заявления. Даже если отказ

        try {
            seasonalServicesService.getOneService(object.getService().getId());
            sendSuccessEmail(object.getUser().getEmail(), object.getService().getName(), object.getService().getDescription());
            repository.toConfirmApplication(result.getId());
        } catch (Exception ex) {
            sendFailEmail(object.getUser().getEmail(), object.getService().getName());
        }
        return result;
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
        object.setUpdatedBy("Manager");
        object.setUpdatedWhen(LocalDateTime.now());
        return super.update(object);
    }
}
