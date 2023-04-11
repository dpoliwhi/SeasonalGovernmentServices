package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.didenko.smartconsulting.seasonalservices.dto.ServiceDtoForManager;
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

    // TODO сделать один маппер  и одну дто
}
