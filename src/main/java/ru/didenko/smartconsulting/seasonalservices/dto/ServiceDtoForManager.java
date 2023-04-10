package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDtoForManager extends GenericDto {

    private String name;

    private String description;

    private LocalDate dateStart;

    private LocalDate dateExpiration;

    private Long balance;
}
