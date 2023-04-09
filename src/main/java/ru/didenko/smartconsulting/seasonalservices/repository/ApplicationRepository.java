package ru.didenko.smartconsulting.seasonalservices.repository;

import ru.didenko.smartconsulting.seasonalservices.model.Application;

import java.util.List;

public interface ApplicationRepository extends GenericRepository<Application> {

    List<Application> findAllByUserId(Long userId);

    List<Application> findAllByServiceId(Long serviceId);
}
