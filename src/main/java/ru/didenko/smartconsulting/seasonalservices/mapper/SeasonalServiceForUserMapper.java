package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDtoForManager;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDtoForUser;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;

@Component
public class SeasonalServiceForUserMapper extends GenericMapper<SeasonalService, ServiceDtoForUser> {
    protected SeasonalServiceForUserMapper(ModelMapper mapper) {
        super(mapper, SeasonalService.class, ServiceDtoForUser.class);
    }
}

