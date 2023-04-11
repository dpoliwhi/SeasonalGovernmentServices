package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.didenko.smartconsulting.seasonalservices.dto.ApplicationRequestDto;
import ru.didenko.smartconsulting.seasonalservices.dto.ApplicationResponseDto;
import ru.didenko.smartconsulting.seasonalservices.mapper.ApplicationRequestMapper;
import ru.didenko.smartconsulting.seasonalservices.mapper.ApplicationResponseMapper;
import ru.didenko.smartconsulting.seasonalservices.model.Application;
import ru.didenko.smartconsulting.seasonalservices.service.ApplicationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/application")
public class ApplicationController extends GenericController<Application, ApplicationRequestDto> {

    private final ApplicationService service;
    private final ApplicationRequestMapper requestMapper;
    private final ApplicationResponseMapper responseMapper;

    public ApplicationController(ApplicationService service, ApplicationRequestMapper mapper, ApplicationResponseMapper responseMapper) {
        super(service, mapper);
        this.service = service;
        this.requestMapper = mapper;
        this.responseMapper = responseMapper;
    }

    @Override
    @Hidden
    public ResponseEntity<ApplicationRequestDto> create(ApplicationRequestDto object) {
        return null;
    }

    @ResponseBody
    @PostMapping("/create-application")
    @Operation(description = "Создать новую заявку", method = "CreateApplication")
    public ResponseEntity<Map<String, Object>> createApplication(@RequestBody ApplicationRequestDto object) {
        Application application = service.create(requestMapper.toEntity(object));
        Map<String, Object> response = service.infoResponse(application);
        response.put("data", requestMapper.toDto(application));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-all-applications-of-user/{id}")
    @Operation(
            description = "Получить список всех заявлений пользователя, отсортированных по дате подачи заявки",
            method = "GetAllApplicationsOfUser")
    public ResponseEntity<List<ApplicationResponseDto>> getAllApplicationsOfUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(responseMapper.toDtos(service.getAllApplicationsOfUser(id)));
    }

    @GetMapping("/get-confirmed-applications-of-user/{id}")
    @Operation(
            description = "Получить список одобренных заявлений пользователя, отсортированных по дате подачи заявки",
            method = "GetConfirmedApplicationsOfUser")
    public ResponseEntity<List<ApplicationResponseDto>> getConfirmedApplicationsOfUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(responseMapper.toDtos(service.getConfirmedApplicationsOfUser(id)));
    }

    @GetMapping("/get-all-confirmed-applications")
    @Operation(
            description = "Получить список всех одобренных заявлений, отсортированных по дате подачи заявки",
            method = "GetConfirmedApplications")
    public ResponseEntity<List<ApplicationResponseDto>> getConfirmedApplication() {
        return ResponseEntity.ok().body(responseMapper.toDtos(service.getConfirmedApplications()));
    }

    @GetMapping("/get-not-confirmed-applications")
    @Operation(
            description = "Получить список всех заявлений c отказом, отсортированных по дате подачи заявки",
            method = "GetNotConfirmedApplication")
    public ResponseEntity<List<ApplicationResponseDto>> getNotConfirmedApplication() {
        return ResponseEntity.ok().body(responseMapper.toDtos(service.getNotConfirmedApplications()));
    }

    @GetMapping("/get-applications-to-one-service/{id}")
    @Operation(
            description = "Получить список заявлений по одной услуге, отсортированных по дате подачи заявки и по статусу одобрения",
            method = "GetApplicationsToOneService")
    public ResponseEntity<List<ApplicationResponseDto>> getApplicationsToOneService(@PathVariable Long id) {
        return ResponseEntity.ok().body(responseMapper.toDtos(service.getApplicationsToOneService(id)));
    }
}
