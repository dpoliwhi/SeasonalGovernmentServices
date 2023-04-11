package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.didenko.smartconsulting.seasonalservices.dto.ApplicationDto;
import ru.didenko.smartconsulting.seasonalservices.mapper.ApplicationMapper;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.service.ApplicationService;

import java.util.Map;

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

    @Override
    @Hidden
    public ResponseEntity<ApplicationDto> create(ApplicationDto object) {
        return null;
    }

    @ResponseBody
    @PostMapping("/create-application")
    @Operation(description = "Создать новую заявку", method = "CreateApplication")
    public ResponseEntity<Map<String, Object>> createApplication(@RequestBody ApplicationDto object) {
        Application application = service.create(mapper.toEntity(object));
        Map<String, Object> response = service.infoResponse(application);
        response.put("data", mapper.toDto(application));
        return ResponseEntity.ok().body(response);
    }
}
