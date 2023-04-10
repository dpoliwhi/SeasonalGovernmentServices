package ru.didenko.smartconsulting.seasonalservices.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.didenko.smartconsulting.seasonalservices.dto.ApplicationDto;
import ru.didenko.smartconsulting.seasonalservices.mapper.ApplicationMapper;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.service.ApplicationService;

@RestController
@RequestMapping("/rest/application")
public class ApplicationController extends GenericController<Application, ApplicationDto> {

    private final ApplicationService service;
    private final ApplicationMapper mapper;

    public ApplicationController(ApplicationService service, ApplicationMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    // TODO выдача поданных заявлений с услугами для юзера

}
