package ru.didenko.smartconsulting.seasonalservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDto extends GenericDto {

    private LocalDateTime createdWhen;

    private String userLogin;

    private String userEmail;

    private String serviceName;

    private String serviceDescription;

    private Boolean isConfirmed;
}
