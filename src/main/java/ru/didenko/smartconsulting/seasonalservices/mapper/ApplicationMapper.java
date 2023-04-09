package ru.didenko.smartconsulting.seasonalservices.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.ApplicationDto;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.repository.SeasonalServiceRepository;
import ru.didenko.smartconsulting.seasonalservices.repository.UserRepository;
import ru.didenko.smartconsulting.seasonalservices.service.SeasonalServicesService;

import javax.annotation.PostConstruct;

@Component
public class ApplicationMapper extends GenericMapper<Application, ApplicationDto> {

    private final UserRepository userRepository;
    private final SeasonalServiceRepository seasonalServiceRepository;

    public ApplicationMapper(ModelMapper mapper, UserRepository userRepository, SeasonalServicesService seasonalServicesService,
                             SeasonalServiceRepository seasonalServiceRepository) {
        super(mapper, Application.class, ApplicationDto.class);
        this.userRepository = userRepository;
        this.seasonalServiceRepository = seasonalServiceRepository;
    }

    @PostConstruct
    public void setupMapper() {
        super.mapper.createTypeMap(Application.class, ApplicationDto.class)
                .addMappings(m -> m.skip(ApplicationDto::setSeasonalServiceId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationDto::setLastName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationDto::setFirstName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationDto::setMidlName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationDto::setEmail)).setPostConverter(toDtoConverter());
        super.mapper.createTypeMap(ApplicationDto.class, Application.class)
                .addMappings(m -> m.skip(Application::setUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Application::setService)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(ApplicationDto source, Application destination) {
        destination.setService(seasonalServiceRepository.findById(source.getSeasonalServiceId()).orElse(null));
        destination.setUser(userRepository.findUserByEmail(source.getEmail()));
    }

    @Override
    void mapSpecificFields(Application source, ApplicationDto destination) {
        destination.setLastName(source.getUser().getLastName());
        destination.setFirstName(source.getUser().getFirstName());
        destination.setMidlName(source.getUser().getMidlName());
        destination.setEmail(source.getUser().getEmail());
        destination.setSeasonalServiceId((source.getService().getId()));
    }
}
