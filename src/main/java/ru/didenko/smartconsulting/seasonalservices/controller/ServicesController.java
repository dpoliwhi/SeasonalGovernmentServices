package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDtoForManager;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDtoForUser;
import ru.didenko.smartconsulting.seasonalservices.exceptions.SpentLimitException;
import ru.didenko.smartconsulting.seasonalservices.mapper.SeasonalServiceForManagerMapper;
import ru.didenko.smartconsulting.seasonalservices.mapper.SeasonalServiceForUserMapper;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;
import ru.didenko.smartconsulting.seasonalservices.service.SeasonalServicesService;

@RestController
@RequestMapping("/rest/services")
public class ServicesController extends GenericController<SeasonalService, ServiceDtoForManager> {

    private final SeasonalServicesService service;
    private final SeasonalServiceForManagerMapper managerMapper;
    private final SeasonalServiceForUserMapper userMapper;


    public ServicesController(SeasonalServicesService service, SeasonalServiceForManagerMapper managerMapper, SeasonalServiceForUserMapper userMapper) {
        super(service, managerMapper);
        this.service = service;
        this.managerMapper = managerMapper;
        this.userMapper = userMapper;
    }

    @ResponseBody
    @GetMapping("/list-allowed")
    @Operation(
            description = "Получить список доступных услуг",
            method = "GetAllowedServices")
    public ResponseEntity<?> getAllowedServices() {
        return ResponseEntity.ok().body(userMapper.toDtos(service.getList()));
    }

    @ResponseBody
    @PutMapping("/get-one-service/{id}")
    @Operation(description = "Получить одну услугу по id", method = "GetTheService")
    public ResponseEntity<?> getTheService(@PathVariable Long id) {
        try {
            service.getOneService(id);
            return ResponseEntity.ok().body(HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }

    }
}
