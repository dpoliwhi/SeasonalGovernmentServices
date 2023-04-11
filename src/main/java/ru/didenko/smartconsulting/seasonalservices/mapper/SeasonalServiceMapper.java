package ru.didenko.smartconsulting.seasonalservices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDto;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;

@Component
public class SeasonalServiceMapper extends GenericMapper<SeasonalService, ServiceDto> {
    protected SeasonalServiceMapper(ModelMapper mapper) {
        super(mapper, SeasonalService.class, ServiceDto.class);
    }
}

