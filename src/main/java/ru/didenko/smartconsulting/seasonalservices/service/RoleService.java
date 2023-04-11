package ru.didenko.smartconsulting.seasonalservices.service;

import org.springframework.stereotype.Service;
import ru.didenko.smartconsulting.seasonalservices.model.Role;
import ru.didenko.smartconsulting.seasonalservices.repository.RoleRepository;

import java.util.List;

/**
 * Class-service with simple methods for Roles
 */
@Service
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> getList() {
        return repository.findAll();
    }

    public Role getOneById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}