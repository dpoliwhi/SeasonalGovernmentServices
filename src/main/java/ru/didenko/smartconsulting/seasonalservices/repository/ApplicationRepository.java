package ru.didenko.smartconsulting.seasonalservices.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.didenko.smartconsulting.seasonalservices.model.Application;

import java.util.List;
import java.util.Set;

public interface ApplicationRepository extends GenericRepository<Application> {

    Set<Application> findAllByIdIn(Set<Long> ids);

    List<Application> findAllByUserId(Long userId);

    List<Application> findAllByServiceId(Long serviceId);

    @Modifying
    @Query(value = "update Application set isConfirmed = true where id = id")
    void toConfirmApplication(Long id);
}
