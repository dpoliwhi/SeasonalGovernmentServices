package ru.didenko.smartconsulting.seasonalservices.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto extends GenericDto {

    private String firstName;

    private String lastName;

    private String midlName;

    private String email;

    private Long seasonalServiceId;
}
