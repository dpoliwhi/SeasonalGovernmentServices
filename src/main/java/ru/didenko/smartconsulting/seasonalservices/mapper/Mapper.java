package ru.didenko.smartconsulting.seasonalservices.mapper;

import ru.didenko.smartconsulting.seasonalservices.dto.GenericDto;
import ru.didenko.smartconsulting.seasonalservices.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDto> {

    E toEntity(D dto);

    D toDto(E entity);

    List<D> toDtos(List<E> entities);
}