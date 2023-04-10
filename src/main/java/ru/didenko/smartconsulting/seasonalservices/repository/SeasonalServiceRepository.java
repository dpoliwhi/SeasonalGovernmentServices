package ru.didenko.smartconsulting.seasonalservices.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.didenko.smartconsulting.seasonalservices.model.SeasonalService;

@Repository
public interface SeasonalServiceRepository extends GenericRepository<SeasonalService> {

    @Modifying
    @Query(value = "update SeasonalService set balance = balance - 1 where id = id")
    void getTheService(Long id);
}
