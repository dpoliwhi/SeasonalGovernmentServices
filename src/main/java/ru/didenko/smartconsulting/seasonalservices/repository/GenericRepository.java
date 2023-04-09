package ru.didenko.smartconsulting.seasonalservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.didenko.smartconsulting.seasonalservices.model.GenericModel;

@NoRepositoryBean
public interface GenericRepository <T extends GenericModel> extends JpaRepository<T, Long> {
}
