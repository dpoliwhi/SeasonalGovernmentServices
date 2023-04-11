package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDto;
import ru.didenko.smartconsulting.seasonalservices.mapper.SeasonalServiceMapper;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;
import ru.didenko.smartconsulting.seasonalservices.service.SeasonalServicesService;

import java.util.List;

@RestController
@RequestMapping("/rest/services")
public class ServicesController extends GenericController<SeasonalService, ServiceDto> {

    private final SeasonalServicesService service;
    private final SeasonalServiceMapper mapper;

    public ServicesController(SeasonalServicesService service, SeasonalServiceMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    @ResponseBody
    @GetMapping("/list-allowed")
    @Operation(
            description = "Получить список доступных услуг",
            method = "GetAllowedServices")
    public ResponseEntity<?> getAllowedServices() {
        return ResponseEntity.ok().body(mapper.toDtos(service.getAllowedServices()));
    }



    public ResponseEntity<?> getList() {
        return ResponseEntity.ok().body(mapper.toDtos(service.getAllowedServices()));
    }

    @Override
    @GetMapping("/list")
    @Operation(
            description = "Получить список всех услуг, отсортированных по дате старта подачи заявок",
            method = "GetAll")
    public ResponseEntity<List<ServiceDto>> getAll() {
        return super.getAll();
    }
}
