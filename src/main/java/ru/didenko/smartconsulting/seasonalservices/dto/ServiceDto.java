package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto extends GenericDto {

    private String name;

    private String description;

    private LocalDate dateStart;

    private LocalDate dateExpiration;

    private Long balance;
}
