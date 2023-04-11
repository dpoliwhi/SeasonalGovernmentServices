package ru.didenko.smartconsulting.seasonalservices.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SeasonalServiceRepository extends GenericRepository<SeasonalService> {

    @Modifying
    @Query(value = "update SeasonalService set balance = balance - 1 where id = :serviceId")
    void getTheService(Long serviceId);

    List<SeasonalService> findAllByOrderByDateStart();

    @Query(value = "select s FROM SeasonalService s where s.dateStart <= :currentDate and s.dateExpiration >= :currentDate and s.balance > 0")
    List<SeasonalService> getAllowedServices(LocalDate currentDate);
}
