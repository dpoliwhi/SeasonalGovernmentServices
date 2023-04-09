package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.didenko.smartconsulting.seasonalservices.model.Application;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithApplicationsDto extends UserDto {

    private Set<ApplicationDto> applications;
}
