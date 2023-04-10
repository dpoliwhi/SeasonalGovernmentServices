package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDtoForUser extends GenericDto {

    private String name;

    private String description;

    private LocalDate dateStart;

    private LocalDate dateExpiration;

    // TODO удалить
}
