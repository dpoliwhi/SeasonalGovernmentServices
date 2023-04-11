package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.ApplicationResponseDto;
import ru.didenko.smartconsulting.seasonalservices.model.Application;

import javax.annotation.PostConstruct;

@Component
public class ApplicationResponseMapper extends GenericMapper<Application, ApplicationResponseDto> {

    public ApplicationResponseMapper(ModelMapper mapper) {
        super(mapper, Application.class, ApplicationResponseDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        super.mapper.createTypeMap(Application.class, ApplicationResponseDto.class)
                .addMappings(m -> m.skip(ApplicationResponseDto::setUserEmail)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationResponseDto::setUserLogin)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationResponseDto::setServiceName)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(ApplicationResponseDto::setServiceDescription)).setPostConverter(toDtoConverter());
    }

    @Override
    void mapSpecificFields(Application source, ApplicationResponseDto destination) {
        destination.setUserLogin(source.getUser().getLogin());
        destination.setUserEmail(source.getUser().getEmail());
        destination.setServiceName((source.getService().getName()));
        destination.setServiceDescription((source.getService().getDescription()));
    }
}
