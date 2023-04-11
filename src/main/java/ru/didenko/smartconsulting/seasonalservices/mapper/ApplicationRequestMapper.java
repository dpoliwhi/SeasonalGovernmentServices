package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.ApplicationRequestDto;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.repository.SeasonalServiceRepository;
import ru.didenko.smartconsulting.seasonalservices.repository.UserRepository;

import javax.annotation.PostConstruct;

@Component
public class ApplicationRequestMapper extends GenericMapper<Application, ApplicationRequestDto> {

    private final UserRepository userRepository;
    private final SeasonalServiceRepository seasonalServiceRepository;

    public ApplicationRequestMapper(ModelMapper mapper, UserRepository userRepository,
                                    SeasonalServiceRepository seasonalServiceRepository) {
        super(mapper, Application.class, ApplicationRequestDto.class);
        this.userRepository = userRepository;
        this.seasonalServiceRepository = seasonalServiceRepository;
    }

    @PostConstruct
    public void setupMapper() {
        super.mapper.createTypeMap(Application.class, ApplicationRequestDto.class)
                .addMappings(m -> m.skip(ApplicationRequestDto::setSeasonalServiceId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationRequestDto::setLogin)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationRequestDto::setLastName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationRequestDto::setFirstName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationRequestDto::setMidlName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationRequestDto::setEmail)).setPostConverter(toDtoConverter());
        super.mapper.createTypeMap(ApplicationRequestDto.class, Application.class)
                .addMappings(m -> m.skip(Application::setUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Application::setService)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(ApplicationRequestDto source, Application destination) {
        destination.setService(seasonalServiceRepository.findById(source.getSeasonalServiceId()).orElse(null));
        destination.setUser(userRepository.findUserByLogin(source.getLogin()));
    }

    @Override
    void mapSpecificFields(Application source, ApplicationRequestDto destination) {
        destination.setLogin(source.getUser().getLogin());
        destination.setLastName(source.getUser().getLastName());
        destination.setFirstName(source.getUser().getFirstName());
        destination.setMidlName(source.getUser().getMidlName());
        destination.setEmail(source.getUser().getEmail());
        destination.setSeasonalServiceId((source.getService().getId()));
    }
}
