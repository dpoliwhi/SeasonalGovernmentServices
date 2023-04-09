package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDtoForManager;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;

@Component
public class SeasonalServiceForManagerMapper extends GenericMapper<SeasonalService, ServiceDtoForManager> {
    protected SeasonalServiceForManagerMapper(ModelMapper mapper) {
        super(mapper, SeasonalService.class, ServiceDtoForManager.class);
    }
}

