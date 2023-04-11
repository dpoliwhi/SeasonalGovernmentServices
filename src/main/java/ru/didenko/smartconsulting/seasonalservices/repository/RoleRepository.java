package ru.didenko.smartconsulting.seasonalservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.didenko.smartconsulting.seasonalservices.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
