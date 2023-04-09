package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ServiceDtoForUser extends GenericDto {

    private String name;

    private String description;

    private LocalDate dateStart;

    private LocalDate dateExpiration;
}
