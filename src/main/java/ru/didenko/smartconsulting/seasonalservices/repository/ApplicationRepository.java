package ru.didenko.smartconsulting.seasonalservices.repository;

import ru.didenko.smartconsulting.seasonalservices.model.Application;

import java.util.List;
import java.util.Set;

public interface ApplicationRepository extends GenericRepository<Application> {

    Set<Application> findAllByIdIn(Set<Long> ids);

    List<Application> findAllByUserId(Long userId);

    List<Application> findAllByServiceId(Long serviceId);
}
