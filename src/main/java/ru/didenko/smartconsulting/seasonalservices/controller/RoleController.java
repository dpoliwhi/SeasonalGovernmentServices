package ru.didenko.smartconsulting.seasonalservices.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.didenko.smartconsulting.seasonalservices.model.Role;
import ru.didenko.smartconsulting.seasonalservices.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/rest/role")
@SecurityRequirement(name = "Bearer Authentication")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<Role> list() {
        return service.getList();
    }
}
