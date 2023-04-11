package ru.didenko.smartconsulting.seasonalservices.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.didenko.smartconsulting.seasonalservices.model.Application;

import java.util.List;
import java.util.Set;

public interface ApplicationRepository extends GenericRepository<Application> {

    Set<Application> findAllByIdIn(Set<Long> ids);

    @Query(value = "select a FROM Application a where a.user.id = :userId order by a.createdWhen")
    List<Application> getAllByUserId(Long userId);

    @Query(value = "select a FROM Application a where a.user.id = :userId and a.isConfirmed = true order by a.createdWhen")
    List<Application> getConfirmedByUserId(Long userId);

    List<Application> getAllByIsConfirmedOrderByCreatedWhen(Boolean isConfirmed);

    @Query(value = "select a FROM Application a where a.service.id = :serviceId order by a.isConfirmed, a.createdWhen")
    List<Application> findAllByServiceId(Long serviceId);

    @Modifying
    @Query(value = "update Application set isConfirmed = true where id = :applicationId")
    void toConfirmApplication(Long applicationId);
}
